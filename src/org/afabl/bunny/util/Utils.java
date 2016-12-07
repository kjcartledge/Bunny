package org.afabl.bunny.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.vfs.ex.VirtualFileManagerAdapter;
import com.intellij.openapi.wm.impl.SystemDock;
import com.intellij.psi.PsiFile;
import com.intellij.util.Base64;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Utils {
  public static final String BUNNY_PROJECT_NAME = "afabl_study";
  public static final String BUNNY_PATH = "/src/main/scala/org/afabl/study/";
  public static final String[] BUNNY_FILENAMES = new String[]{
          "AfablTask1.scala", "AfablTask2.scala",
          "scalaTask1.scala", "scalaTask2.scala"};

  public boolean isTrackedFile(@Nullable VirtualFile file) {
    if (null == file) {
      return false;
    }
    for (String filename : BUNNY_FILENAMES) {
      if (file.getPath().endsWith(filename)) {
        return true;
      }
    }
    return false;
  }

  public boolean isTrackedProject(@NotNull Project project) {
    return project.getName().equals(BUNNY_PROJECT_NAME);
  }

  public VirtualFile getActiveFile(@NotNull Project project) {
    FileEditorManager manager = FileEditorManager.getInstance(project);
    if (null == manager) {
      return null;
    }
    Editor editor = manager.getSelectedTextEditor();
    if (null != editor) {
      return FileDocumentManager.getInstance().getFile(editor.getDocument());
    } else {
      return null;
    }
  }

  public String getFilesJson(Project project) {
    StringBuilder builder = new StringBuilder();
    builder.append("\"files\":[");
    for (int i = 0; i < BUNNY_FILENAMES.length; i++) {
      String path = "file://" + project.getBasePath() + BUNNY_PATH + BUNNY_FILENAMES[i];
      VirtualFile file = VirtualFileManager.getInstance().findFileByUrl(path);
      if (null != file) {
        try {
          builder.append("{\"contents\":\"")
                 .append(Base64.encode(file.contentsToByteArray()))
                 .append("\",\"charset\":\"")
                 .append(file.getCharset())
                 .append("\"}");
          if (i != (BUNNY_FILENAMES.length - 1)) {
            builder.append(",");
          }
        } catch (IOException e) {
          // Do nothing...
        }
      }
    }
    builder.append("]");
    return builder.toString();
  }
}
