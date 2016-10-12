package org.afabl.bunny.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import org.afabl.bunny.dialog.ExportResultsDialog;
import org.afabl.bunny.state.BunnyHistory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ExportResultsAction extends AnAction {

    private static Logger logger =
            Logger.getInstance(ExportResultsAction.class);

    private BunnyHistory history;

    public ExportResultsAction(BunnyHistory history) {
        super("Export Results");
        this.history = history;
    }

    @Override
    public void actionPerformed(final AnActionEvent e) {
        history.addEvent(BunnyHistory.Action.EXPORT_OPEN);
        new ExportResultsDialog(e.getProject(), new ExportResultsDialog.Listener() {
            @Override
            public void onOKAction(String id, File directory) {
                history.setUserId(id);
                history.addEvent(BunnyHistory.Action.EXPORT_OK,
                                 BunnyHistory.Action.STUDY_ENDED);
                String export = history.exportAsString();
                ApplicationManager.getApplication()
                        .runWriteAction(new ExportFileTask(export, directory));
            }
            @Override
            public void onCancelAction() {
                history.addEvent(BunnyHistory.Action.EXPORT_CANCEL);
            }
        }).show();
    }

    private class ExportFileTask implements Runnable {

        private String text;
        private File directory;

        public ExportFileTask(String text, File directory) {
            this.text = text;
            this.directory = directory;
        }

        @Override
        public void run() {
            try {
                PrintWriter writer = new PrintWriter(new File(directory,
                        "afabl_study_results.txt"));
                writer.print(text);
                writer.close();
            } catch (Exception e) {
                logger.warn("Failed to export results!", e);
            }
        }
    }
}
