package org.afabl.bunny.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.afabl.bunny.dialog.SubmitDialog;
import org.afabl.bunny.state.BunnyHistory;

public class SubmitAction extends AnAction {

    private BunnyHistory history;

    public SubmitAction(BunnyHistory history) {
        super("Submit");
        this.history = history;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        history.addEvent(BunnyHistory.Action.SUBMIT_OPEN);
        new SubmitDialog(e.getProject(), new SubmitDialog.Listener() {
            @Override
            public void onOKAction(String id) {
                history.setUserId(id);
                history.addEvent(BunnyHistory.Action.SUBMIT_OK);
            }
            @Override
            public void onCancelAction() {
                history.addEvent(BunnyHistory.Action.SUBMIT_CANCEL);
            }
        }).show();
    }
}
