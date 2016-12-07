package org.afabl.bunny.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import java.net.URI;
import org.afabl.bunny.dialog.SubmitDialog;
import org.afabl.bunny.util.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class SubmitAction extends AnAction {

  public static final String BUNNY_SUBMIT_ACTION_NAME = "BunnySubmit";
  public static final String BUNNY_SUBMIT_MENU_STRING = "Submit";

  private static Logger logger = Logger.getInstance(SubmitAction.class);
  private static final String SUBMIT_URL = "http://bunny.afabl.org/results/";
  private static final String SURVEY_URL = "https://docs.google.com/forms/d/e/1FAIpQLScaYWjobvIGr2b"
      + "HTZMgAEV5GzbFftzCkNKs8UlnD3a2v-eFhw/viewform?entry.2071527133&entry.1804496195&entry.20861"
      + "94466=";

  private final Supplier<String> jsonSupplier;
  private final Supplier<String> idSupplier;
  private final ActionListener listener;

  public SubmitAction(@NotNull Supplier<String> jsonSupplier,
                      @NotNull Supplier<String> idSupplier,
                      @Nullable ActionListener listener) {
    super(BUNNY_SUBMIT_MENU_STRING);
    this.jsonSupplier = jsonSupplier;
    this.idSupplier = idSupplier;
    this.listener = null == listener ? ActionListener.NOOP : listener;
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    listener.start();
    new SubmitDialog(e.getProject(), new SubmitDialog.Listener() {
      @Override
      public void onOKAction(String id, SubmitDialog.Callback callback) {
        listener.id(id);
        ApplicationManager.getApplication().runWriteAction(new SubmitFileTask(id, callback));
      }

      @Override
      public void onCancelAction() {
        listener.abort();
      }
    }, idSupplier.get()).show();
  }

  private class SubmitFileTask implements Runnable {

    private final String userId;
    private final SubmitDialog.Callback callback;

    /* package */ SubmitFileTask(@NotNull final String userId,
                                 @NotNull final SubmitDialog.Callback callback) {
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
        Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        writer.write(jsonSupplier.get());
        writer.flush();
        writer.close();
        int result = conn.getResponseCode();
        if (result < 0) {
          logger.warn("Submission failed. No response received from the server.");
          listener.failure("No server response");
          callback.onFailure("Submission failed. No response received from the server.");
        } else if (result != HTTP_OK) {
          logger.warn("Submission failed with response: " + conn.getResponseMessage());
          listener.failure("Bad response: " + conn.getResponseMessage());
          callback.onFailure("Submission failed with response: " + conn.getResponseMessage());
        } else {
          java.awt.Desktop.getDesktop().browse(URI.create(SURVEY_URL + userId));
          listener.success();
          callback.onSuccess();
        }
      } catch (MalformedURLException e) {
        logger.warn("Unable to submit as the URL is malformed.", e);
        listener.failure(e);
        callback.onFailure("An unrecoverable internal error occurred. Please use the manual export "
                + "option instead.");
      } catch (IOException e) {
        logger.warn("Unable to submit as an I/O error occurred.", e);
        listener.failure(e);
        callback.onFailure("Submission failed. A connection error while trying to submit. Please "
                + "ensure you are connected to the internet and try again.");
      }
    }
  }
}
