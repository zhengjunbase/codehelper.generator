package com.ccnode.codegenerator.service.register;

import com.ccnode.codegenerator.enums.RequestType;
import com.ccnode.codegenerator.pojo.BaseResponse;
import com.ccnode.codegenerator.pojoHelper.ServerRequestHelper;
import com.ccnode.codegenerator.service.pojo.RegisterRequest;
import com.ccnode.codegenerator.service.pojo.RegisterResponse;
import com.ccnode.codegenerator.service.pojo.ServerResponse;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.HttpUtil;
import com.ccnode.codegenerator.util.JSONUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.SecurityHelper;
import org.slf4j.Logger;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/16 21:29
 */
public class RegisterService {

    private final static Logger LOGGER = LoggerWrapper.getLogger(RegisterService.class);

    private static String URL = "http://115.28.149.106:8080/generator/register";

    public static ServerResponse register(String license) {

        ServerResponse ret = new ServerResponse();
        long startTime = System.currentTimeMillis();
        try {
            RegisterRequest request = new RegisterRequest();
            request.setRequestType(RequestType.REGISTER.name());
            request = ServerRequestHelper.fillCommonField(request);
            request.setLicense(license);
            String s = HttpUtil.postJson(URL, request);
            RegisterResponse response = JSONUtil.parseObject(s, RegisterResponse.class);
            saveRegisterResponse(response,license);

        } catch (Throwable e) {

        } finally {

        }

        return ret;
    }

    private static void saveRegisterResponse(RegisterResponse response, String license) {
        if (BaseResponse.SUCCESS.equals(response.getCode())) {
            Date expiredDate = response.getExpiredDate();
            String key = SecurityHelper.encryptDate("fascias", expiredDate);
            String el = SecurityHelper.encrypt("fascias",license);
            SettingService.getInstance().getState().getKeyList().add(key);
            SettingService.getInstance().getState().getTkeyList().add(el);
        }
    }

    public static void main(String[] args) {
        String license = "dakjfalsfsjdf";
        ServerResponse register = register(license);
        System.out.println(register);
    }
}
