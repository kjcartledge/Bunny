package org.afabl.bunny.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ExportResultsDialog extends DialogWrapper {
    private JPanel contentPane;
    private JButton selectDirectoryButton;
    private File selectedDirectory;
    private JFormattedTextField participantIdField;
    private Listener listener;

    public ExportResultsDialog(@Nullable Project project,
                               @NotNull Listener listener) {
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
        init();
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
        super.doOKAction();
        listener.onOKAction((String) participantIdField.getValue(),
                            selectedDirectory);
    }

    @Override
    public void doCancelAction() {
        super.doCancelAction();
        listener.onCancelAction();
    }

    public interface Listener {
        void onOKAction(String id, File directory);
        void onCancelAction();
    }
}