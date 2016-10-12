package org.afabl.bunny;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.afabl.bunny.dialog.InfoDialog;
import org.afabl.bunny.state.BunnyHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.afabl.bunny.BunnyComponent.BUNNY_FILENAME;

public class BunnyEditorListener implements FileEditorManagerListener,
        AWTEventListener, ActionListener {

    private static final Logger logger =
            Logger.getInstance(BunnyEditorListener.class);

    private final Project project;
    private final BunnyHistory history;
    private final Timer timer;
    private boolean idle;

    /* package */ BunnyEditorListener(Project project, BunnyHistory history,
                                      Timer timer) {
        this.project = project;
        this.history = history;
        this.timer = timer;
        this.idle = true; // Will avoid going idle until after user input.
    }

    @Override
    public void fileOpened(@NotNull final FileEditorManager source,
                           @NotNull final VirtualFile file) {
        if (file.getPath().endsWith(BUNNY_FILENAME)) {
            if (!history.getStarted()) {
                history.addEvent(BunnyHistory.Action.INFO_OPEN);
                new InfoDialog(project, new InfoDialog.Listener() {
                    @Override
                    public void onOKAction() {
                        history.setStarted(true);
                        history.addEvent(BunnyHistory.Action.INFO_OK,
                                         BunnyHistory.Action.STUDY_STARTED);
                    }
                    @Override
                    public void onCancelAction() {
                        source.closeFile(file);
                        history.addEvent(BunnyHistory.Action.INFO_CANCEL);
                    }
                }).show();
            }
            history.addEvent(BunnyHistory.Action.FILE_OPEN);
        }
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source,
                           @NotNull VirtualFile file) {
        if (file.getPath().endsWith(BUNNY_FILENAME)) {
            history.addEvent(BunnyHistory.Action.FILE_CLOSED);
        }
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        if (null != event.getOldFile()
                && event.getOldFile().getPath().endsWith(BUNNY_FILENAME)) {
            history.addEvent(BunnyHistory.Action.FILE_INACTIVE);
        } else if (null != event.getNewFile()
                && event.getNewFile().getPath().endsWith(BUNNY_FILENAME)) {
            history.addEvent(BunnyHistory.Action.FILE_ACTIVE);
        }
    }

    @Override
    public void eventDispatched(AWTEvent event) {
        idle(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        idle(true);
    }

    private synchronized void idle(boolean idle) {
        if (!this.idle) {
            if (idle) {
                timer.stop();
                this.idle = true;
                history.addEvent(BunnyHistory.Action.USER_IDLE);
            } else {
                timer.restart();
            }
        } else if (!idle) {
            timer.start();
            this.idle = false;
            history.addEvent(BunnyHistory.Action.USER_ACTIVE);
        }
    }
}
