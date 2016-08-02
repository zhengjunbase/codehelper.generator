package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.pojo.RegisterRawRequest;
import com.ccnode.codegenerator.service.pojo.RegisterRequest;
import com.ccnode.codegenerator.util.JSONUtil;
import com.ccnode.codegenerator.util.SecurityHelper;
import org.jetbrains.annotations.NotNull;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/28 18:34
 */
public class RegisterRawRequestHelper {

    public static RegisterRequest parseFromRaw(@NotNull RegisterRawRequest rawRequest){
        RegisterRequest ret = new RegisterRequest();
        ret.setPluginVersion(SecurityHelper.decrypt(rawRequest.getKey1()));
        ret.setSystem(SecurityHelper.decrypt(rawRequest.getKey2()));
        ret.setIp(SecurityHelper.decrypt(rawRequest.getKey3()));
        ret.setMacAddressList(SecurityHelper.decrypt(rawRequest.getKey4()));
        ret.setLicense(SecurityHelper.decrypt(rawRequest.getKey5()));
        ret.setRequestType(SecurityHelper.decrypt(rawRequest.getKey6()));
        ret.setUserType(SecurityHelper.decrypt(rawRequest.getKey7()));
        return ret;
    }
    
    public static RegisterRawRequest buildRawRequest(RegisterRequest request){
        RegisterRawRequest ret = new RegisterRawRequest();
        ret.setKey1(SecurityHelper.encrypt(request.getPluginVersion()));
        ret.setKey2(SecurityHelper.encrypt(request.getSystem()));
        ret.setKey3(SecurityHelper.encrypt(request.getIp()));
        ret.setKey4(SecurityHelper.encrypt(request.getMacAddressList()));
        ret.setKey5(SecurityHelper.encrypt(request.getLicense()));
        ret.setKey6(SecurityHelper.encrypt(request.getRequestType()));
        ret.setKey7(SecurityHelper.encrypt(request.getUserType()));
        return ret;
    }
    





    @NotNull
    public static RegisterRequest parseFromRawJson(@NotNull String json){

        RegisterRawRequest rawRequest = JSONUtil.parseObject(json, RegisterRawRequest.class);
        return parseFromRaw(rawRequest);
    }


}
