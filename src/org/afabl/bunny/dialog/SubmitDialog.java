package org.afabl.bunny.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SubmitDialog extends DialogWrapper {
  private JPanel contentPane;
  private JFormattedTextField participantIdField;
  private JProgressBar progressBar;
  private JSeparator progressBarSeparator;
  private Listener listener;

  public SubmitDialog(@Nullable Project project,
                      @NotNull Listener listener,
                      @Nullable String participantId) {
    super(project);
    this.listener = listener;
    participantIdField.setFormatterFactory(
            new ParticipantIdFormatterFactory());
    if (null != participantId) {
      participantIdField.setValue(participantId);
    }
    progressBar.setVisible(false);
    progressBarSeparator.setVisible(false);
    init();
    this.setSize(500, 600);
    this.setResizable(false);
    setTitle("Submit");
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return contentPane;
  }

  @Nullable
  @Override
  protected ValidationInfo doValidate() {
    if (!participantIdField.isEditValid()) {
      return new ValidationInfo("Enter your 8-Digit Participant ID.",
              participantIdField);
    } else {
      return null;
    }
  }

  @Override
  public void doCancelAction() {
    super.doCancelAction();
    listener.onCancelAction();
  }

  @Override
  protected void doOKAction() {
    progressBar.setVisible(true);
    progressBarSeparator.setVisible(true);
    repaint();
    listener.onOKAction((String) participantIdField.getValue(),
            new Callback() {
              @Override
              public void onSuccess() {
                SubmitDialog.super.doOKAction();
              }

              @Override
              public void onFailure(String message) {
                progressBar.setVisible(false);
                progressBarSeparator.setVisible(false);
                setErrorText(message);
              }
            });
  }

  /**
   * Represents a listener of {@link SubmitDialog} events (i.e. The class that performs the submit).
   */
  public interface Listener {
    /**
     * Invoked when the OK button is clicked on the {@link SubmitDialog}.
     * @param id the User ID to submit with
     * @param callback a {@link Callback} that should be invoked on success or failure
     */
    void onOKAction(String id, Callback callback);

    /**
     * Invoked when the Cancel button is clicked on the {@link SubmitDialog}.
     */
    void onCancelAction();
  }

  /**
   * A callback used to update the {@link SubmitDialog} on success or failure.
   */
  public interface Callback {
    /**
     * Invoked on export success.
     */
    void onSuccess();

    /**
     * Invoked on export failure along with a message to display explaining the error encountered.
     * @param message a user-readable explanation of the error
     */
    void onFailure(String message);
  }
}
