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
    private Listener listener;

    public SubmitDialog(@Nullable Project project, @NotNull Listener listener) {
        super(project);
        this.listener = listener;
        participantIdField.setFormatterFactory(
                new ParticipantIdFormatterFactory());
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
        super.doOKAction();
        listener.onOKAction((String) participantIdField.getValue());
    }

    public interface Listener {
        void onOKAction(String id);

        void onCancelAction();
    }
}
