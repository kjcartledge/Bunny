package org.afabl.bunny.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import org.afabl.bunny.Study;
import org.afabl.bunny.dialog.SubmitDialog;
import org.afabl.bunny.dialog.SubmitDialog.Callback;
import org.afabl.bunny.dialog.SubmitDialog.Listener;
import org.afabl.bunny.state.BunnyHistory;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class SubmitAction extends AnAction {

    private static Logger logger = Logger.getInstance(SubmitAction.class);
    private static final String SUBMIT_URL = "http://bunny.afabl.org/results/";

    private final BunnyHistory history;
    private final Study study;

    public SubmitAction(BunnyHistory history, Study study) {
        super("Submit");
        this.history = history;
        this.study = study;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        history.addEvent(BunnyHistory.Action.SUBMIT_OPEN);
        new SubmitDialog(e.getProject(), new Listener() {
            @Override
            public void onOKAction(String id, Callback callback) {
                history.setUserId(id);
                history.addEvent(BunnyHistory.Action.SUBMIT_OK);
                ApplicationManager.getApplication().runWriteAction(
                        new SubmitFileTask(id, callback));
            }
            @Override
            public void onCancelAction() {
                history.addEvent(BunnyHistory.Action.SUBMIT_CANCEL);
            }
        }, history.getUserId()).show();
    }

    private class SubmitFileTask implements Runnable {

        private final String userId;
        private final Callback callback;

        /* package */ SubmitFileTask(@NotNull final String userId,
                                     @NotNull final Callback callback) {
            this.userId = userId;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(SUBMIT_URL + userId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                Writer writer = new BufferedWriter(new OutputStreamWriter(
                        conn.getOutputStream()));
                writer.write(history.exportAsString());
                writer.flush();
                writer.close();
                int result = conn.getResponseCode();
                if (result < 0) {
                    logger.warn("Submission failed. No response received from "
                            + "the server.");
                    history.addEvent(BunnyHistory.Action.SUBMIT_FAIL);
                    callback.onFailure("Submission failed. No response received"
                            + " from the server.");
                } else if (result != HTTP_OK) {
                    logger.warn("Submission failed with response: "
                            + conn.getResponseMessage());
                    history.addEvent(BunnyHistory.Action.SUBMIT_FAIL);
                    callback.onFailure("Submission failed with response: "
                            + conn.getResponseMessage());
                } else {
                    history.addEvent(BunnyHistory.Action.SUBMIT_SUCCESS,
                                     BunnyHistory.Action.STUDY_ENDED);
                    study.end();
                    callback.onSuccess();
                }
            } catch (MalformedURLException e) {
                logger.warn("Unable to submit as the URL is malformed.", e);
                history.addEvent(BunnyHistory.Action.SUBMIT_FAIL);
                callback.onFailure("An unrecoverable internal error occurred."
                        + " Please use the manual export option instead.");
            } catch (IOException e) {
                logger.warn("Unable to submit as an I/O error occurred.", e);
                history.addEvent(BunnyHistory.Action.SUBMIT_FAIL);
                callback.onFailure("Submission failed. A connection error "
                        + "while trying to submit. Please ensure you are "
                        + "connected to the internet and try again.");
            }
        }
    }
}
