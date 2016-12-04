package org.afabl.bunny.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public final class Utils {
  public static final String BUNNY_PROJECT_NAME = "afabl_study";
  public static final String[] BUNNY_FILENAMES = new String[]{
          "AfablTask1.scala", "AfablTask2.scala",
          "scalaTask1.scala", "scalaTask2.scala"};

  public boolean isTrackedFile(VirtualFile file) {
    for (String filename : BUNNY_FILENAMES) {
      if (file.getPath().endsWith(filename)) {
        return true;
      }
    }
    return false;
  }

  public boolean isTrackedProject(Project project) {
    return project.getName().equals(BUNNY_PROJECT_NAME);
  }
}
