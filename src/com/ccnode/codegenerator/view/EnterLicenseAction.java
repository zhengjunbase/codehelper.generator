package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.service.pojo.ServerResponse;
import com.ccnode.codegenerator.service.register.CheckRegisterService;
import com.ccnode.codegenerator.service.register.RegisterService;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.DateUtil;
import com.ccnode.codegenerator.util.SecurityHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class EnterLicenseAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        String tipMsg = "Please enter license Key : ";
        if(CheckRegisterService.checkFromLocal()){
            int keySize = SettingService.getInstance().getState().getKeyList().size();
            String key = SettingService.getInstance().getState().getKeyList().get(keySize - 1);
            int tkeySize = SettingService.getInstance().getState().getTkeyList().size();
            String tkey = SettingService.getInstance().getState().getTkeyList().get(tkeySize - 1);
            Date expiredDate = SecurityHelper.decryptToDate(key);
            if(expiredDate != null){
                String license = SecurityHelper.decrypt(tkey).toUpperCase();
                String expiredDateStr = DateUtil.formatLong(expiredDate);
                if( expiredDate.compareTo(new Date()) <= 0){
                    tipMsg = license + "\nhas been expired at " + expiredDateStr + ",\n Please enter new license Key :";
                }
                if( expiredDate.compareTo(new Date()) > 0 && SettingService.getInstance().canUsePremium()){
                    Messages.showMessageDialog(project,"License: " +license + "\nExpired Date: " + expiredDateStr, "Register Success", Messages.getInformationIcon());
                    return;
                }
            }
        }
        String license = Messages.showInputDialog(project, tipMsg, "Entry License", Messages.getQuestionIcon());
        if(StringUtils.isBlank(license)){
            Messages.showMessageDialog(project, "Register Failure", "Information", Messages.getInformationIcon());
            return;
        }
        ServerResponse response = RegisterService.register(license);
        if(response.checkSuccess() && SettingService.getInstance().canUsePremium()){
            Messages.showMessageDialog(project, "Register Success", "Information", Messages.getInformationIcon());
        }else{
            Messages.showMessageDialog(project, "Register Failure", "Information", Messages.getInformationIcon());
        }
    }
}