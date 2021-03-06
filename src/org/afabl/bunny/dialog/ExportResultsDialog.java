package org.afabl.bunny.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ExportResultsDialog extends DialogWrapper {
  private JPanel contentPane;
  private JButton selectDirectoryButton;
  private File selectedDirectory;
  private JFormattedTextField participantIdField;
  private JProgressBar progressBar;
  private JSeparator progressBarSeparator;
  private Listener listener;

  public ExportResultsDialog(@Nullable Project project,
                             @NotNull Listener listener,
                             @Nullable String participantId) {
    super(project);
    this.listener = listener;
    participantIdField.setFormatterFactory(
            new ParticipantIdFormatterFactory());
    selectDirectoryButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser chooser =
                new JFileChooser(System.getProperty("user.home"));
        chooser.setDialogTitle("Select a Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(ExportResultsDialog.this.contentPane)
                == JFileChooser.APPROVE_OPTION) {
          selectedDirectory = chooser.getSelectedFile();
          selectDirectoryButton.setText(selectedDirectory.getPath());
        } else {
          selectedDirectory = null;
          selectDirectoryButton.setText("Select Directory");
        }
      }
    });
    if (null != participantId) {
      participantIdField.setValue(participantId);
    }
    progressBar.setVisible(false);
    progressBarSeparator.setVisible(false);
    init();
    this.setSize(500, 600);
    this.setResizable(false);
    setTitle("Export Results");
  }

  @Nullable
  @Override
  protected ValidationInfo doValidate() {
    if (!participantIdField.isEditValid()) {
      return new ValidationInfo("Enter your 8-Digit Participant ID.",
              participantIdField);
    } else if (null == selectedDirectory
            || !selectedDirectory.exists()
            || !selectedDirectory.isDirectory()) {
      return new ValidationInfo("Select a directory to export results "
              + "to.", selectDirectoryButton);
    } else if (!selectedDirectory.canWrite()) {
      return new ValidationInfo("Select a directory that can be written "
              + "to.", selectDirectoryButton);
    } else {
      return null;
    }
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return contentPane;
  }

  @Override
  protected void doOKAction() {
    progressBar.setVisible(true);
    progressBarSeparator.setVisible(true);
    repaint();
    listener.onOKAction((String) participantIdField.getValue(),
            selectedDirectory, new Callback() {
              @Override
              public void onSuccess() {
                ExportResultsDialog.super.doOKAction();
              }

              @Override
              public void onFailure(String message) {
                progressBar.setVisible(false);
                progressBarSeparator.setVisible(false);
                setErrorText(message);
              }
            });
  }

  @Override
  public void doCancelAction() {
    super.doCancelAction();
    listener.onCancelAction();
  }

  /**
   * Represents a listener of {@link ExportResultsDialog} events (i.e. The class that performs the
   * submit).
   */
  public interface Listener {
    /**
     * Invoked when the OK button is clicked on the {@link ExportResultsDialog}.
     * @param id the User ID to submit with
     * @param directory the {@like File} of the directory to export results to
     * @param callback a {@link Callback} that should be invoked on success or failure
     */
    void onOKAction(String id, File directory, Callback callback);

    /**
     * Invoked when the Cancel button is clicked on the {@link ExportResultsDialog}.
     */
    void onCancelAction();
  }

  /**
   * A callback used to update the {@link ExportResultsDialog} on success or failure.
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
