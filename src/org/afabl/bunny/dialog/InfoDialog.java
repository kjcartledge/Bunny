package org.afabl.bunny.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfoDialog extends DialogWrapper {
  private JPanel contentPane;
  private Listener listener;

  public InfoDialog(@Nullable Project project, @NotNull Listener listener) {
    super(project);
    this.listener = listener;
    init();
    this.setSize(500, 600);
    this.setResizable(false);
    setTitle("Welcome");
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return contentPane;
  }

  @Override
  protected void doOKAction() {
    super.doOKAction();
    listener.onOKAction();
  }

  @Override
  public void doCancelAction() {
    super.doCancelAction();
    listener.onCancelAction();
  }

  /**
   * Represents a listener of {@link InfoDialog} events.
   */
  public interface Listener {
    /**
     * Invoked when the OK button is clicked on the {@link InfoDialog}.
     */
    void onOKAction();

    /**
     * Invoked when the Cancel button is clicked on the {@link InfoDialog}.
     */
    void onCancelAction();
  }
}
