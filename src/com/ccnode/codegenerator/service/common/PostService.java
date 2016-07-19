package com.ccnode.codegenerator.service.common;

import com.ccnode.codegenerator.enums.RequestType;
import com.ccnode.codegenerator.service.pojo.ServerRequest;
import com.ccnode.codegenerator.service.pojo.ServerResponse;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.HttpUtil;
import com.ccnode.codegenerator.util.JSONUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:39
 */
public class PostService {

    private static String URL = "www.codehelper.me/generator";

    public static ServerResponse post(ServerRequest request) {
        long startTime = System.currentTimeMillis();
        try{
            String json = JSONUtil.toJSONString(request);
            String response = HttpUtil.postJson(URL, json);

        }catch(Throwable e){
        }finally{
        }

        ServerResponse ret = new ServerResponse();
        ret.setRequest(request);
        ret.setRequestType(request.getRequestType());
        ret.setReturnKey(UUID.randomUUID().toString());
        ret.success();
        return ret;
    }

    public static void processRequest(ServerRequest request) {
        try {
            ServerResponse response = post(request);
            if (RequestType.REGISTER.equalWithName(request.getRequestType())) {
                if (StringUtils.isNotBlank(response.getReturnKey())) {
                    SettingService.getInstance().getState().setUserType(response.getUserType());
                    SettingService.getInstance().getState().getReturnKeyMap().put(response.getReturnKey(), new Date());
                }
            }

        } catch (Throwable e) {

        }
    }
}
