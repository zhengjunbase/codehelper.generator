package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.service.pojo.ServerResponse;
import com.ccnode.codegenerator.service.register.RegisterCheckService;
import com.ccnode.codegenerator.service.register.RegisterService;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.DateUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.SecurityHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/16 21:30
 */
public class EnterLicenseAction {

    private final static Logger LOGGER = LoggerWrapper.getLogger(EnterLicenseAction.class);

//    @Override
//    public void actionPerformed(AnActionEvent event) {
//        Project project = event.getData(PlatformDataKeys.PROJECT);
//        String tipMsg = SettingService.getInstance().getState().getRegisterTipMsg();
//        if(RegisterCheckService.checkFromLocal()){
//            String eKey = SettingService.getInstance().getState().geteKey();
//            String lKey = SettingService.getInstance().getState().getlKey();
//            Date expiredDate = SecurityHelper.decryptToDate(eKey);
//            if(expiredDate != null){
//                String license = SecurityHelper.decrypt(lKey).toUpperCase();
//                String expiredDateStr = DateUtil.formatLong(expiredDate);
//                if( expiredDate.compareTo(new Date()) <= 0){
//                    tipMsg = license + "\nhas been expired at " + expiredDateStr + ",\n Please enter new license Key :";
//                }
//                if( expiredDate.compareTo(new Date()) > 0 && SettingService.getInstance().isDonated()){
//                    Messages.showMessageDialog(project,"License: " +license + "\nExpired Date: " + expiredDateStr, "Register Success", Messages.getInformationIcon());
//                    return;
//                }
//            }
//        }else{
//            tipMsg = "Please Enter a License:";
//        }
//        ShowRegisterDialog(project, tipMsg);
//    }
//
//    public static void ShowRegisterDialog(@Nullable Project project, String tipMsg) {
//        try{
//            String license = Messages.showInputDialog(project, tipMsg, "Entry License", Messages.getQuestionIcon());
//            if(StringUtils.isBlank(license)){
//                Messages.showMessageDialog(project, "Register Failure", "Information", Messages.getInformationIcon());
//                return;
//            }
//            ServerResponse response = RegisterService.register(license);
//            if(response.checkSuccess() && SettingService.getInstance().isDonated()){
//                Messages.showMessageDialog(project, "Register Success", "Information", Messages.getInformationIcon());
//            }else{
//                Messages.showMessageDialog(project, "Register Failure", "Information", Messages.getInformationIcon());
//            }
//        }catch(Throwable e){
//            LOGGER.error("ShowRegisterDialog, error ",e);
//        }
//
//    }
}