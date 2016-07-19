package com.ccnode.codegenerator.view;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class EnterLicenseAction extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
                String txt = Messages
                .showInputDialog(project, "Please enter license Key : ", "Entry License", Messages.getQuestionIcon());
//        Messages.showMessageDialog(project, "click www.codehelper.me", "Information", Messages.getInformationIcon());
    }
}