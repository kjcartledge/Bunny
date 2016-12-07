package org.afabl.bunny;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.fileEditor.TextEditor;
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

public class BunnyEditorListener implements FileEditorManagerListener, AWTEventListener {

  private static final Logger logger = Logger.getInstance(BunnyEditorListener.class);

  private final Project project;
  private final Utils utils;
  private final Timer idleTimer;
  private final Timer statusTimer;
  private final Study study;
  private boolean idle;

  /* package */ BunnyEditorListener(final Project project, Study study, final int idleDelay) {
    this.project = project;
    this.utils = new Utils();
    this.study = study;
    this.idleTimer = new Timer(idleDelay, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        idle(true);
      }
    });
    this.statusTimer = new Timer(idleDelay, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        VirtualFile active = utils.getActiveFile(project);
        if (utils.isTrackedFile(active)) {
          BunnyEditorListener.this.study.event(new Action(idle ? Action.Type.USER_IDLE :
                  Action.Type.USER_ACTIVE, active.getName()));
        }
      }
    });
    this.idle = true; // Will avoid going idle until after user input.
    idleTimer.start();
    statusTimer.start();
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

  public void stopTimers() {
    idleTimer.stop();
    statusTimer.stop();
  }

  @Override
  public void eventDispatched(AWTEvent event) {
    idle(false);
  }

  private synchronized void idle(boolean idle) {
    if (!this.idle) {
      if (idle) {
        idleTimer.stop();
        this.idle = true;
        study.event(new Action(Action.Type.USER_IDLE));
      } else {
        idleTimer.restart();
      }
    } else if (!idle) {
      idleTimer.start();
      this.idle = false;
      study.event(new Action(Action.Type.USER_ACTIVE));
    }
  }
}
