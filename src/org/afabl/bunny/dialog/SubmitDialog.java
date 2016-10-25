package org.afabl.bunny.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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

    public interface Listener {
        void onOKAction(String id, Callback callback);
        void onCancelAction();
    }

    public interface Callback {
        void onSuccess();
        void onFailure(String message);
    }
}
