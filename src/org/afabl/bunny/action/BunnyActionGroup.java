package org.afabl.bunny.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
        import com.intellij.openapi.actionSystem.CommonDataKeys;
        import com.intellij.openapi.actionSystem.DefaultActionGroup;
        import com.intellij.openapi.actionSystem.Presentation;

public class BunnyActionGroup extends DefaultActionGroup {

    public BunnyActionGroup() {
        this.disableIfNoVisibleChildren();
    }

    @Override
    public void update(AnActionEvent event) {
        Presentation p = event.getPresentation();
        boolean hasProject = event.getData(CommonDataKeys.PROJECT) != null;
        p.setVisible(hasProject);
    }
}