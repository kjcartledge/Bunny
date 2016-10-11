package org.afabl.bunny.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class InfoDialog extends DialogWrapper {
    private JPanel contentPane;

    public InfoDialog(@Nullable Project project) {
        super(project, true, IdeModalityType.MODELESS);
        init();
        this.setResizable(false);
        setTitle("Welcome");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }
}
