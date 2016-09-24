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
import com.intellij.openapi.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/09/17 18:26
 */
public class SendToServerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SendToServerService.class);

    public static PostResponse postToServer(Project project, GenCodeResponse genCodeResponse){
        long startTime = System.currentTimeMillis();
        try{

            Boolean checkSuccess = RegisterCheckService.checkAll();
            if(!checkSuccess){
                String tipMsg = "Licence Check Register Failure.\n Please Entry a New License:";
                SettingService.getInstance().getState().setRegisterTipMsg(tipMsg);
                EnterLicenseAction.ShowRegisterDialog(project, tipMsg);
                return new PostResponse().failure("Check Failure");
            }

            SendToServerRequest request = new SendToServerRequest();
            ServerRequestHelper.fillCommonField(request);
            List<ChangeInfo> changeInfos = Lists.newArrayList();
            changeInfos.addAll(genCodeResponse.getUpdateFiles());
            changeInfos.addAll(genCodeResponse.getNewFiles());
            request.setChangeInfos(changeInfos);
            request.setErrorMsg(LoggerWrapper.errorList);
            if(genCodeResponse.getThrowable() != null){
                request.setStackTraceMsg(Lists.newArrayList(Throwables.getStackTraceAsString(genCodeResponse.getThrowable())));
            }
            String s = HttpUtil.postJsonEncrypt(UrlManager.POST_URL, request);
            PostResponse response = JSONUtil.parseObject(SecurityHelper.decrypt(s), PostResponse.class);
            return response.success();

        }catch(Throwable e){
            LOGGER.error("SendToServerService postToServer error", e);
            return new PostResponse().failure("Exception");
        }finally{
            LOGGER.info("SendToServerService postToServer cost:{}", System.currentTimeMillis() - startTime);
        }
    }
}
