package com.ccnode.codegenerator.server.service;

import com.ccnode.codegenerator.enums.RequestType;
import com.ccnode.codegenerator.enums.UserType;
import com.ccnode.codegenerator.server.pojo.ServerRequest;
import com.ccnode.codegenerator.server.pojo.ServerResponse;
import com.ccnode.codegenerator.storage.SettingService;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:39
 */
public class PostService {

    private static String URL = "";

    public static ServerResponse post(ServerRequest request){
        ServerResponse ret = new ServerResponse();
        ret.setRequest(request);
        ret.setRequestType(RequestType.fromName(request.getRequestType()));
        ret.setReturnKey(UUID.randomUUID().toString());
        ret.success();
        return ret;
    }

    public static void processRequest(ServerRequest request){
        try{
            ServerResponse response = post(request);
            if(response.getRequestType() == RequestType.REGISTER){
                if(StringUtils.isNotBlank(response.getReturnKey())){
                    SettingService.getInstance().getState().setUserType(UserType.PAID_YEAR_USER.name());
                }
            }

        }catch(Exception e){

        }
    }
}
