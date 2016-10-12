package org.afabl.bunny.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class InfoDialog extends DialogWrapper {
    private JPanel contentPane;
    private Listener listener;

    public InfoDialog(@Nullable Project project, @NotNull Listener listener) {
        super(project);
        this.listener = listener;
        init();
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

    public interface Listener {
        void onOKAction();

        void onCancelAction();
    }
}
