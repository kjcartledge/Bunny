package org.afabl.bunny;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Watcher implements ProjectComponent {

    private static final Logger logger = Logger.getInstance(Watcher.class);

    private final Project project;
    private EditorListener listener;

    public Watcher(Project project) {
        this.project = project;
        listener = new EditorListener(project);
    }

    @Override
    public void initComponent() {
        MessageBus bus = ApplicationManager.getApplication().getMessageBus();
        MessageBusConnection connection = bus.connect();
        connection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER,
                listener);
    }

    @Override
    public void disposeComponent() {

    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {

    }

    @Override
    @NotNull
    public String getComponentName() {
        return "Bunny.Watcher";
    }
}
