package com.ccnode.codegenerator.service;

import com.ccnode.codegenerator.enums.UrlManager;
import com.ccnode.codegenerator.pojo.ChangeInfo;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojoHelper.ServerRequestHelper;
import com.ccnode.codegenerator.service.pojo.PostResponse;
import com.ccnode.codegenerator.service.pojo.SendToServerRequest;
import com.ccnode.codegenerator.service.register.RegisterCheckService;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.HttpUtil;
import com.ccnode.codegenerator.util.JSONUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.SecurityHelper;
import com.ccnode.codegenerator.view.EnterLicenseAction;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.browsers.WebBrowserManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/09/17 18:26
 */
public class SendToServerService {

    private final static Logger LOGGER = LoggerWrapper.getLogger(SendToServerService.class);

    public static void postToCheck(Project project, GenCodeResponse genCodeResponse){
        long startTime = System.currentTimeMillis();
        try{

            Boolean checkSuccess = RegisterCheckService.checkAll();
            if(!checkSuccess && RegisterCheckService.checkFromLocal()){
                String tipMsg = "Licence Check Register Failure.\n Please Entry a New License:";
                SettingService.getInstance().getState().setRegisterTipMsg(tipMsg);
                EnterLicenseAction.ShowRegisterDialog(project, tipMsg);
            }

        }catch(Throwable e){
            LOGGER.error("SendToServerService postToCheck error", e);
        }finally{
            LOGGER.info("SendToServerService postToCheck cost:{}", System.currentTimeMillis() - startTime);
        }
    }

    public static void postToServer(Project project, GenCodeResponse genCodeResponse){
        long startTime = System.currentTimeMillis();
        try{
            SendToServerRequest request = new SendToServerRequest();
            ServerRequestHelper.fillCommonField(request);
            List<ChangeInfo> changeInfos = Lists.newArrayList();
            if(genCodeResponse.getUpdateFiles() != null)
            changeInfos.addAll(genCodeResponse.getUpdateFiles());
            changeInfos.addAll(genCodeResponse.getNewFiles());
            request.setChangeInfos(changeInfos);
            request.setSettingDto(SettingService.getInstance().getState());
            List<String> errorList = Lists.newArrayList();
            for (String s : LoggerWrapper.errorList) {
                if(StringUtils.isNotBlank(s)){
                    errorList.add(StringUtils.deleteWhitespace(s));
                }
            }
            request.setErrorMsg(errorList);

            if(genCodeResponse.getThrowable() != null){
                request.setStackTraceMsg(Lists.newArrayList(Throwables.getStackTraceAsString(genCodeResponse.getThrowable())));
            }
            String s = HttpUtil.postJsonEncrypt(UrlManager.POST_URL + "?id=" + SettingService.getUUID(), request);
            PostResponse serverMsg = JSONUtil.parseObject(SecurityHelper.decrypt(s), PostResponse.class);
            if(serverMsg != null && serverMsg.checkSuccess() && serverMsg.getHasServerMsg()){
                int result = Messages
                        .showOkCancelDialog(project, serverMsg.getContent(), serverMsg.getTitle(), "OK", serverMsg.getButtonStr(), null);
                if(result == 2 && StringUtils.isNotBlank(serverMsg.getButtonUrl())){
                    BrowserLauncher.getInstance().browse(serverMsg.getButtonUrl(), WebBrowserManager.getInstance().getFirstActiveBrowser());
                }
            }
        }catch(Throwable e){
            LOGGER.error("SendToServerService postToServer error,{}", e);
        }finally{
            LOGGER.info("SendToServerService postToServer cost:{}", System.currentTimeMillis() - startTime);
        }

    }
}
