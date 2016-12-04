package org.afabl.bunny.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import org.afabl.bunny.dialog.ExportResultsDialog;
import org.afabl.bunny.util.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class ExportResultsAction extends AnAction {

  public static final String BUNNY_EXPORT_ACTION_NAME = "BunnyExport";
  public static final String BUNNY_EXPORT_MENU_STRING = "Export Results";

  private static Logger logger = Logger.getInstance(ExportResultsAction.class);

  private final Supplier<String> jsonSupplier;
  private final Supplier<String> idSupplier;
  private final ActionListener listener;

  public ExportResultsAction(@NotNull Supplier<String> jsonSupplier,
                             @NotNull Supplier<String> idSupplier,
                             @Nullable ActionListener listener) {
    super(BUNNY_EXPORT_MENU_STRING);
    this.jsonSupplier = jsonSupplier;
    this.idSupplier = idSupplier;
    this.listener = null == listener ? ActionListener.NOOP : listener;
  }

  @Override
  public void actionPerformed(final AnActionEvent e) {
    listener.start();
    new ExportResultsDialog(e.getProject(), new ExportResultsDialog.Listener() {
      @Override
      public void onOKAction(String id, File directory, ExportResultsDialog.Callback callback) {
        listener.id(id);
        ApplicationManager.getApplication().runWriteAction(new ExportFileTask(directory, callback));
      }

      @Override
      public void onCancelAction() {
        listener.abort();
      }
    }, idSupplier.get()).show();
  }

  private class ExportFileTask implements Runnable {

    private final File directory;
    private final ExportResultsDialog.Callback callback;

    /* package */ ExportFileTask(@NotNull final File directory,
                                 @NotNull final ExportResultsDialog.Callback callback) {
      this.directory = directory;
      this.callback = callback;
    }

    @Override
    public void run() {
      try {
        PrintWriter writer = new PrintWriter(new FileOutputStream(
                new File(directory, "afabl_results.txt"), false));
        writer.print(jsonSupplier.get());
        writer.close();
        listener.success();
        callback.onSuccess();
      } catch (Exception e) {
        logger.warn("Results export failed: ", e);
        listener.failure(e);
        callback.onFailure("Unable to export results. Check the "
                + "directory's permissions and try again.");
      }
    }
  }
}
