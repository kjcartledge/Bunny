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
import org.afabl.bunny.action.BunnyActionGroup;
import org.afabl.bunny.action.ExportResultsAction;
import org.afabl.bunny.action.SubmitAction;
import org.afabl.bunny.state.BunnyHistory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class BunnyComponent implements ProjectComponent, Study {

    public static final String BUNNY_FILENAME = "bunny.scala";
    public static final String BUNNY_PROJECT_NAME = "afabl_study";
    public static final int IDLE_DELAY = 30000; // 30 Seconds
    public static final String BUNNY_ACTION_GROUP_NAME = "BunnyToolsGroup";
    public static final String BUNNY_SUBMIT_ACTION_NAME = "BunnySubmit";
    public static final String BUNNY_EXPORT_ACTION_NAME = "BunnyExport";

    private static final Logger logger = Logger.getInstance(BunnyComponent.class);

    private final Project project;
    private final String projectName;
    private BunnyEditorListener listener;
    private BunnyHistory history;
    private MessageBusConnection connection;
    private Timer timer;
    private ActionManager actionManager;
    private BunnyActionGroup actionGroup;
    private SubmitAction submitAction;
    private ExportResultsAction exportAction;
    private boolean initCompleted;

    public BunnyComponent(Project project) {
        this.project = project;
        projectName = project.getName();
        initCompleted = false;
        if (isBunnyProject()) {
            history = BunnyHistory.getInstance(project);
            timer = new Timer(IDLE_DELAY, null);
            listener = new BunnyEditorListener(project, history, timer, this);
            actionManager = ActionManager.getInstance();
            actionGroup = (BunnyActionGroup) actionManager
                    .getAction(BUNNY_ACTION_GROUP_NAME);
            submitAction = new SubmitAction(history, this);
            exportAction = new ExportResultsAction(history, this);
        }
    }

    @Override
    public void initComponent() {
        if (isBunnyProject()) {
            MessageBus bus = ApplicationManager.getApplication().getMessageBus();
            connection = bus.connect();
            connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                    listener);
            Toolkit.getDefaultToolkit().addAWTEventListener(listener,
                    AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
            timer.addActionListener(listener);
            if (history.getStarted()) {
                start();
            }
            initCompleted = true;
        }
    }

    @Override
    public void disposeComponent() {
        if (initCompleted) {
            connection.disconnect();
            timer.removeActionListener(listener);
            timer.stop();
            actionGroup.remove(submitAction);
            actionManager.unregisterAction(BUNNY_SUBMIT_ACTION_NAME);
            actionGroup.remove(exportAction);
            actionManager.unregisterAction(BUNNY_EXPORT_ACTION_NAME);
            submitAction = null;
            actionGroup = null;
            actionManager = null;
            timer = null;
            history = null;
            listener = null;
        }
    }

    @Override
    public void projectOpened() {
        if (isBunnyProject()) {
            history.addEvent(BunnyHistory.Action.PROJECT_OPEN);
        }
    }

    @Override
    public void projectClosed() {
        if (isBunnyProject()) {
            history.addEvent(BunnyHistory.Action.PROJECT_CLOSE);
        }
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "Bunny.BunnyComponent";
    }

    private boolean isBunnyProject() {
        return projectName.equals(BUNNY_PROJECT_NAME);
    }

    @Override
    public void start() {
        actionManager.registerAction(BUNNY_SUBMIT_ACTION_NAME,
                submitAction);
        actionGroup.add(submitAction);
        actionManager.registerAction(BUNNY_EXPORT_ACTION_NAME,
                exportAction);
        actionGroup.add(exportAction);
    }

    @Override
    public void end() {
        FileEditorManager manager = FileEditorManager.getInstance(project);
        VirtualFile[] files = manager.getOpenFiles();
        for (VirtualFile file : files) {
            if (file.getPath().endsWith(BUNNY_FILENAME)) {
                manager.closeFile(file);
                return;
            }
        }
    }
}
