package org.afabl.bunny;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class EditorListener implements FileEditorManagerListener {

    private static final Logger logger =
            Logger.getInstance(EditorListener.class);
    private static final String sourceFilename = "bunny.scala";
    private static final String logFilename = "bunny.log";

    private final Project project;
    private final Application app;
    private PrintWriter logWriter;
    private File logfile;
    private boolean failure;


    /* package */ EditorListener(Project project) {
        this.project = project;
        this.app =  ApplicationManager.getApplication();
        this.logfile = new File(project.getBaseDir().getPath() + "/"
                + logFilename);
        this.failure = false;
    }

    @Override
    public void fileOpened(@NotNull final FileEditorManager source,
                           @NotNull final VirtualFile file) {
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source,
                           @NotNull VirtualFile file) {
        if (failure) {
            return;
        }
        logger.warn("Writing bunny.log to: " + logfile);
        logWriter.printf("closed:%d\n", System.nanoTime());
        logWriter.close();
        VirtualFileManager.getInstance().asyncRefresh(null);
        logWriter = null;
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
        if (failure) {
            return;
        }
        if (null != event.getOldFile()
                && event.getOldFile().getPath().endsWith(sourceFilename)) {
            logTime("inactive");
        } else if (null != event.getNewFile()
                && event.getNewFile().getPath().endsWith(sourceFilename)) {
            logTime("active");
        }
    }

    private synchronized void logTime(String tag) {
        long time = System.nanoTime();
        if (null == logWriter) {
            try {
                logWriter = new PrintWriter(new FileWriter(logfile, true));
                logger.warn("Created bunny.log writer.");
                logWriter.printf("opened:%d\n", time);
            } catch (FileNotFoundException e) {
                logger.error("Unable to create file: " + logfile, e);
                failure = true;
                return;
            } catch (IOException e) {
                logger.error("Unable to create file: " + logfile, e);
                failure = true;
                return;
            }
        }
        logWriter.printf("%s:%d\n", tag, time);
        logger.warn(tag + ":" + time);
    }
}