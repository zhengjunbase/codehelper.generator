package com.ccnode.codegenerator.service.register;

import com.ccnode.codegenerator.enums.RequestType;
import com.ccnode.codegenerator.pojo.BaseResponse;
import com.ccnode.codegenerator.pojo.RegisterRawRequest;
import com.ccnode.codegenerator.pojo.RegisterRawResponse;
import com.ccnode.codegenerator.pojoHelper.RegisterRawRequestHelper;
import com.ccnode.codegenerator.pojoHelper.RegisterRawResponseHelper;
import com.ccnode.codegenerator.pojoHelper.ServerRequestHelper;
import com.ccnode.codegenerator.service.pojo.RegisterRequest;
import com.ccnode.codegenerator.service.pojo.RegisterResponse;
import com.ccnode.codegenerator.service.pojo.ServerResponse;
import com.ccnode.codegenerator.storage.SettingDto;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.HttpUtil;
import com.ccnode.codegenerator.util.JSONUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.SecurityHelper;
import com.intellij.openapi.components.ServiceManager;
import org.slf4j.Logger;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/16 21:29
 */
public class RegisterService {

    private final static Logger LOGGER = LoggerWrapper.getLogger(RegisterService.class);

    private static String REGISTER_URL = "www.codehelper.me/generator/register";

    public static ServerResponse register(String license) {

        ServerResponse ret = new ServerResponse();
        long startTime = System.currentTimeMillis();
        try {
            RegisterRequest request = new RegisterRequest();
            request.setRequestType(RequestType.REGISTER.name());
            request = ServerRequestHelper.fillCommonField(request);
            request.setLicense(license);
            RegisterRawRequest rawRequest = RegisterRawRequestHelper.buildRawRequest(request);
            String s = HttpUtil.postJson(REGISTER_URL, rawRequest);
            RegisterRawResponse rawResponse = JSONUtil.parseObject(s, RegisterRawResponse.class);
            RegisterResponse response = RegisterRawResponseHelper.parseRawResponse(rawResponse);
            saveRegisterResponse(response,license);
            return response;

        } catch (Throwable e) {
            LOGGER.error("register error", e);
            return ret;
        }

    }

    private static void saveRegisterResponse(RegisterResponse response, String license) {
        if (BaseResponse.SUCCESS.equals(response.getStatus())) {
            try{
                Date expiredDate = response.getExpireDate();
                String key = SecurityHelper.encryptDate( expiredDate);
                String el = SecurityHelper.encrypt("fascias",license);
                SettingService setting = ServiceManager.getService(SettingService.class);
                SettingDto state = setting.getState();
                state.getKeyList().add(key);
                state.getTkeyList().add(el);
            }catch(Throwable e){
                LOGGER.error("RegisterService saveRegisterResponse error", e);
            }

        }
    }

    public static void main(String[] args) {
        String license = "dakjfalsfsjdf";
        ServerResponse register = register(license);
        System.out.println(register);
    }
}
