package org.afabl.bunny;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import javax.swing.Timer;
import org.afabl.bunny.action.ActionListener;
import org.afabl.bunny.action.BunnyActionGroup;
import org.afabl.bunny.action.ExportResultsAction;
import org.afabl.bunny.action.SubmitAction;
import org.afabl.bunny.state.Action;
import org.afabl.bunny.state.History;
import org.afabl.bunny.util.Function;
import org.afabl.bunny.util.Supplier;
import org.afabl.bunny.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static org.afabl.bunny.action.BunnyActionGroup.BUNNY_ACTION_GROUP_NAME;
import static org.afabl.bunny.action.ExportResultsAction.BUNNY_EXPORT_ACTION_NAME;
import static org.afabl.bunny.action.SubmitAction.BUNNY_SUBMIT_ACTION_NAME;

public class BunnyComponent implements ProjectComponent {

  public static final int IDLE_DELAY = 30000; // 30 Seconds
  public static final String BUNNY_COMPONENT_NAME = "Bunny.BunnyComponent";

  private static final Logger logger = Logger.getInstance(BunnyComponent.class);

  private final Project project;
  private final Utils utils;
  private BunnyEditorListener listener;
  private History history;
  private MessageBusConnection connection;
  private Timer timer;
  private boolean initCompleted;
  private final Study study;

  public BunnyComponent(Project project) {
    this.project = project;
    this.utils = new Utils();
    initCompleted = false;
    if (utils.isTrackedProject(project)) {
      history = History.getInstance(project);
      timer = new Timer(IDLE_DELAY, null);
      study = new Study(new Function() {
        @Override
        public void call() {
          ActionManager actionManager = ActionManager.getInstance();
          BunnyActionGroup actionGroup = (BunnyActionGroup) actionManager
                  .getAction(BUNNY_ACTION_GROUP_NAME);
          Supplier<String> jsonSupplier = new Supplier<String>() {
            @Override
            public String get() {
              return study.getJson();
            }
          };
          Supplier<String> idSupplier = new Supplier<String>() {
            @Override
            public String get() {
              return study.getUserId();
            }
          };
          SubmitAction submitAction = new SubmitAction(jsonSupplier, idSupplier,
                  new SubmitActionListener());
          ExportResultsAction exportAction = new ExportResultsAction(jsonSupplier, idSupplier,
                  new SubmitActionListener());
          actionManager.registerAction(BUNNY_SUBMIT_ACTION_NAME, submitAction);
          actionGroup.add(submitAction);
          actionManager.registerAction(BUNNY_EXPORT_ACTION_NAME, exportAction);
          actionGroup.add(exportAction);
        }
      }, new Function() {
        @Override
        public void call() {
          FileEditorManager manager = FileEditorManager.getInstance(BunnyComponent.this.project);
          for (VirtualFile file : manager.getOpenFiles()) {
            if (utils.isTrackedFile(file)) {
              manager.closeFile(file);
            }
          }
        }
      }, history);
      listener = new BunnyEditorListener(project, study, timer);
    } else {
      study = null;
    }
  }

  @Override
  public void initComponent() {
    if (utils.isTrackedProject(project)) {
      MessageBus bus = ApplicationManager.getApplication().getMessageBus();
      connection = bus.connect();
      connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, listener);
      Toolkit.getDefaultToolkit().addAWTEventListener(listener,
              AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
      timer.addActionListener(listener);
      initCompleted = true;
    }
  }

  @Override
  public void disposeComponent() {
    if (initCompleted) {
      connection.disconnect();
      timer.removeActionListener(listener);
      timer.stop();
      ActionManager actionManager = ActionManager.getInstance();
      BunnyActionGroup actionGroup = (BunnyActionGroup) actionManager
              .getAction(BUNNY_ACTION_GROUP_NAME);
      actionGroup.removeAll();
      actionManager.unregisterAction(BUNNY_SUBMIT_ACTION_NAME);
      actionManager.unregisterAction(BUNNY_EXPORT_ACTION_NAME);
      timer = null;
      history = null;
      listener = null;
    }
  }

  @Override
  public void projectOpened() {
    if (utils.isTrackedProject(project)) {
      study.event(new Action(Action.Type.PROJECT_OPEN, project.getName()));
    }
  }

  @Override
  public void projectClosed() {
    if (utils.isTrackedProject(project)) {
      study.event(new Action(Action.Type.PROJECT_CLOSE, project.getName()));
    }
  }

  @Override
  @NotNull
  public String getComponentName() {
    return BUNNY_COMPONENT_NAME;
  }

  private class SubmitActionListener implements ActionListener {

    @Override
    public void start() {
      study.event(new Action(Action.Type.SUBMIT_OPEN));
    }

    @Override
    public void id(@NotNull String id) {
      study.setUserId(id);
    }

    @Override
    public void failure(@Nullable Object data) {
      study.event(new Action(Action.Type.SUBMIT_FAIL, data));
    }

    @Override
    public void success() {
      study.event(new Action(Action.Type.SUBMIT_SUCCESS));
    }

    @Override
    public void abort() {
      study.event(new Action(Action.Type.SUBMIT_CANCEL));
    }
  }

  private class ExportResultsActionListener implements ActionListener {

    @Override
    public void start() {
      study.event(new Action(Action.Type.EXPORT_OPEN));
    }

    @Override
    public void id(@NotNull String id) {
      study.setUserId(id);
    }

    @Override
    public void failure(@Nullable Object data) {
      study.event(new Action(Action.Type.EXPORT_FAIL, data));
    }

    @Override
    public void success() {
      study.event(new Action(Action.Type.EXPORT_SUCCESS));
    }

    @Override
    public void abort() {
      study.event(new Action(Action.Type.EXPORT_CANCEL));
    }
  }
}
