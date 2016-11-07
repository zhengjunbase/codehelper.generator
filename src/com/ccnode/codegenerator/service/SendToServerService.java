package com.ccnode.codegenerator.service;

import com.ccnode.codegenerator.enums.UrlManager;
import com.ccnode.codegenerator.pojo.ChangeInfo;
import com.ccnode.codegenerator.pojo.GenCodeResponse;
import com.ccnode.codegenerator.pojoHelper.ServerRequestHelper;
import com.ccnode.codegenerator.service.pojo.PostResponse;
import com.ccnode.codegenerator.service.pojo.GenCodeServerRequest;
import com.ccnode.codegenerator.service.pojo.ServerRequest;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.HttpUtil;
import com.ccnode.codegenerator.util.JSONUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.intellij.ide.browsers.BrowserLauncher;
import com.intellij.ide.browsers.WebBrowserManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/09/17 18:26
 */
public class SendToServerService {

    private final static Logger LOGGER = LoggerWrapper.getLogger(SendToServerService.class);

//    public static void postToCheck(Project project, GenCodeResponse genCodeResponse){
//        long startTime = System.currentTimeMillis();
//        try{
//
//            Boolean checkSuccess = RegisterCheckService.checkAll();
//            if(!checkSuccess && RegisterCheckService.checkFromLocal()){
//                String tipMsg = "Licence Check Register Failure.\n Please Entry a New License:";
//                SettingService.getInstance().getState().setRegisterTipMsg(tipMsg);
////                EnterLicenseAction.ShowRegisterDialog(project, tipMsg);
//            }
//
//        }catch(Throwable e){
//            LOGGER.error("SendToServerService postToCheck error", e);
//        }finally{
//            LOGGER.info("SendToServerService postToCheck cost:{}", System.currentTimeMillis() - startTime);
//        }
//    }

    public static GenCodeServerRequest buildGenCodeRequest(GenCodeResponse genCodeResponse){
        long startTime = System.currentTimeMillis();
        try{
            GenCodeServerRequest request = new GenCodeServerRequest();
            ServerRequestHelper.fillCommonField(request);
            List<ChangeInfo> changeInfos = Lists.newArrayList();
            if(genCodeResponse.getUpdateFiles() != null) {
                changeInfos.addAll(genCodeResponse.getUpdateFiles());
            }
            if(genCodeResponse.getNewFiles() != null){
                changeInfos.addAll(genCodeResponse.getNewFiles());
            }
            request.setChangeInfos(changeInfos);
            request.setSettingDto(SettingService.getInstance().getState());


            if(genCodeResponse.getThrowable() != null){
                request.setStackTraceMsg(Lists.newArrayList(
                        StringUtils.deleteWhitespace(Throwables.getStackTraceAsString(genCodeResponse.getThrowable()))));
            }
            return request;
        }catch(Throwable e){
            return null;
        }

    }

    public static void post(Project project, ServerRequest request){
        try{
             List<String> errorList = Lists.newArrayList();
            for (String s : LoggerWrapper.errorList) {
                if(StringUtils.isNotBlank(s)){
                    errorList.add(StringUtils.deleteWhitespace(s));
                }
            }
            request.setErrorList(errorList);

            String s = HttpUtil.postJson(UrlManager.getPostUrl() + "&type="+request.getRequestType(), request);
            LOGGER.info("ret:{}",s);
            if(StringUtils.isBlank(s) || !StringUtils.containsIgnoreCase(s,"success")){
                return;
            }
            PostResponse serverMsg = JSONUtil.parseObject(s, PostResponse.class);

            if(serverMsg != null && serverMsg.getHasServerMsg()){
                int result = Messages
                        .showOkCancelDialog(project, serverMsg.getContent(), serverMsg.getTitle(), "OK", serverMsg.getButtonStr(), null);
                if(result == 2 && StringUtils.isNotBlank(serverMsg.getButtonUrl())){
                    BrowserLauncher.getInstance().browse(serverMsg.getButtonUrl(), WebBrowserManager.getInstance().getFirstActiveBrowser());
                }
                SettingService.getSetting().geteKeyList().add(String.valueOf(serverMsg.getMsgId()));
            }
        }catch(Throwable ignored){

        }


    }

}
