package org.afabl.bunny.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import org.afabl.bunny.Study;
import org.afabl.bunny.dialog.ExportResultsDialog;
import org.afabl.bunny.dialog.ExportResultsDialog.Callback;
import org.afabl.bunny.dialog.ExportResultsDialog.Listener;
import org.afabl.bunny.state.BunnyHistory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ExportResultsAction extends AnAction {

    private static Logger logger =
            Logger.getInstance(ExportResultsAction.class);

    private final BunnyHistory history;
    private final Study study;

    public ExportResultsAction(BunnyHistory history, Study study) {
        super("Export Results");
        this.history = history;
        this.study = study;
    }

    @Override
    public void actionPerformed(final AnActionEvent e) {
        history.addEvent(BunnyHistory.Action.EXPORT_OPEN);
        new ExportResultsDialog(e.getProject(), new Listener() {
            @Override
            public void onOKAction(String id, File directory, Callback callback) {
                history.setUserId(id);
                history.addEvent(BunnyHistory.Action.EXPORT_OK);
                String export = history.exportAsString();
                ApplicationManager.getApplication().runWriteAction(
                        new ExportFileTask(directory, callback));
            }
            @Override
            public void onCancelAction() {
                history.addEvent(BunnyHistory.Action.EXPORT_CANCEL);
            }
        }, history.getUserId()).show();
    }

    private class ExportFileTask implements Runnable {

        private final File directory;
        private final Callback callback;

        /* package */ ExportFileTask(@NotNull final File directory,
                                     @NotNull final Callback callback) {
            this.directory = directory;
            this.callback = callback;
        }

        @Override
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(new FileOutputStream(
                        new File(directory, "afabl_results.txt"), false));
                writer.print(history.exportAsString());
                writer.close();
                history.addEvent(BunnyHistory.Action.EXPORT_SUCCESS,
                                 BunnyHistory.Action.STUDY_ENDED);
                study.end();
                callback.onSuccess();
            } catch (Exception e) {
                logger.warn("Results export failed: ", e);
                history.addEvent(BunnyHistory.Action.EXPORT_FAIL);
                callback.onFailure("Unable to export results. Check the "
                        + "directory's permissions and try again.");
            }
        }
    }
}
