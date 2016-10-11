package org.afabl.bunny;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.afabl.bunny.state.BunnyHistory;
import org.jetbrains.annotations.NotNull;

import static org.afabl.bunny.BunnyComponent.BUNNY_FILENAME;

public class BunnyEditorListener implements FileEditorManagerListener {

    private static final Logger logger =
            Logger.getInstance(BunnyEditorListener.class);

    private final Project project;
    private final BunnyHistory history;


    /* package */ BunnyEditorListener(Project project, BunnyHistory history) {
        this.project = project;
        this.history = history;
    }

    @Override
    public void fileOpened(@NotNull final FileEditorManager source,
                           @NotNull final VirtualFile file) {
        if (file.getPath().endsWith(BUNNY_FILENAME)) {
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
}