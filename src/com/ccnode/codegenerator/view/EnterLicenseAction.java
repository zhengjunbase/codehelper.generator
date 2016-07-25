package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.service.pojo.RegisterRequest;
import com.ccnode.codegenerator.service.pojo.ServerResponse;
import com.ccnode.codegenerator.service.register.RegisterService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class EnterLicenseAction extends AnAction {

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        String license = Messages.showInputDialog(project, "Please enter license Key : ", "Entry License", Messages.getQuestionIcon());
        if(StringUtils.isBlank(license)){
            Messages.showMessageDialog(project, "Register Failure", "Information", Messages.getInformationIcon());
            return;
        }
        ServerResponse response = RegisterService.register(license);
        if(response.checkSuccess()){
            Messages.showMessageDialog(project, "Register Success", "Information", Messages.getInformationIcon());
        }else{
            Messages.showMessageDialog(project, "Register Failure", "Information", Messages.getInformationIcon());
        }
    }
}