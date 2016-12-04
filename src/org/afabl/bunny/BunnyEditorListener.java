package org.afabl.bunny;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.awt.AWTEvent;
import javax.swing.Timer;
import org.afabl.bunny.dialog.InfoDialog;
import org.afabl.bunny.state.Action;
import org.afabl.bunny.util.Utils;
import org.jetbrains.annotations.NotNull;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BunnyEditorListener implements FileEditorManagerListener, AWTEventListener,
        ActionListener {

  private static final Logger logger = Logger.getInstance(BunnyEditorListener.class);

  private final Project project;
  private final Utils utils;
  private final Timer timer;
  private final Study study;
  private boolean idle;

  /* package */ BunnyEditorListener(Project project, Study study, Timer timer) {
    this.project = project;
    this.utils = new Utils();
    this.timer = timer;
    this.study = study;
    this.idle = true; // Will avoid going idle until after user input.
  }

  @Override
  public void fileOpened(@NotNull final FileEditorManager source, @NotNull final VirtualFile file) {
    if (utils.isTrackedFile(file)) {
      if (!study.isStarted()) {
        study.event(new Action(Action.Type.INFO_OPEN));
        new InfoDialog(project, new InfoDialog.Listener() {
          @Override
          public void onOKAction() {
            study.start();
            study.event(new Action(Action.Type.INFO_OK));
          }
          @Override
          public void onCancelAction() {
            // TODO: Closing the file here can cause Intellij to crash, find a fix if possible.
            //source.closeFile(file);
            // TODO: Remove start() call here when fix to above is found.
            study.start();
            study.event(new Action(Action.Type.INFO_CANCEL));
          }
        }).show();
      }
      study.event(new Action(Action.Type.FILE_OPEN, file.getName()));
    }
  }

  @Override
  public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
    if (utils.isTrackedFile(file)) {
      study.event(new Action(Action.Type.FILE_CLOSED, file.getName()));
    }
  }

  @Override
  public void selectionChanged(@NotNull FileEditorManagerEvent event) {
    if (null != event.getOldFile() && utils.isTrackedFile(event.getOldFile())) {
      study.event(new Action(Action.Type.FILE_INACTIVE, event.getOldFile().getName()));
    }
    if (null != event.getNewFile() && utils.isTrackedFile(event.getNewFile())) {
      study.event(new Action(Action.Type.FILE_ACTIVE, event.getNewFile().getName()));
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
        study.event(new Action(Action.Type.USER_IDLE));
      } else {
        timer.restart();
      }
    } else if (!idle) {
      timer.start();
      this.idle = false;
      study.event(new Action(Action.Type.USER_ACTIVE));
    }
  }
}
