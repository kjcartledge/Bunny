package org.afabl.bunny;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.afabl.bunny.state.BunnyHistory;
import org.jetbrains.annotations.NotNull;

public class BunnyComponent implements ProjectComponent {

    public static final String BUNNY_FILENAME = "bunny.scala";
    public static final String BUNNY_PROJECT_NAME = "afabl_study";

    private static final Logger logger = Logger.getInstance(BunnyComponent.class);

    private final Project project;
    private final String projectName;
    private BunnyEditorListener listener;
    private BunnyHistory history;
    private MessageBusConnection connection;

    public BunnyComponent(Project project) {
        this.project = project;
        projectName = project.getName();
        if (isBunnyProject()) {
            history = BunnyHistory.getInstance(project);
            listener = new BunnyEditorListener(project, history);

        }
    }

    @Override
    public void initComponent() {
        if (isBunnyProject()) {
            MessageBus bus = ApplicationManager.getApplication().getMessageBus();
            connection = bus.connect();
            connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                    listener);
        }
    }

    @Override
    public void disposeComponent() {
        connection.disconnect();
        history = null;
        listener = null;
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
}
