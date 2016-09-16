package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.pojo.RegisterRawResponse;
import com.ccnode.codegenerator.service.pojo.RegisterResponse;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.SecurityHelper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/08/02 09:44
 */
public class RegisterRawResponseHelper {

    private final static Logger LOGGER = LoggerWrapper.getLogger(RegisterRawResponseHelper.class);

    public static RegisterRawResponse buildRawResponse(@NotNull RegisterResponse response){
        RegisterRawResponse ret = new RegisterRawResponse();
        ret.setKey1(SecurityHelper.encrypt(response.getLicense()));
        if(response.getExpireDate() != null){
            ret.setKey2(SecurityHelper.encryptDate(response.getExpireDate()));
        }
        ret.setKey3(SecurityHelper.decrypt(response.getLicenseStatus()));
        ret.setKey4(SecurityHelper.decrypt(response.getLicenseType()));
        ret.setMsg(response.getMsg());
        ret.setCode(response.getCode());
        ret.setStatus(response.getStatus());
        return ret;
    }
    
    
    public static RegisterResponse parseRawResponse(@NotNull RegisterRawResponse rawResponse){
        RegisterResponse ret = new RegisterResponse();
        ret.setLicense(SecurityHelper.decrypt(rawResponse.getKey1()));
        if(rawResponse.getKey2() != null){
            ret.setExpireDate(SecurityHelper.decryptToDate(rawResponse.getKey2()));
        }
        ret.setLicenseStatus(SecurityHelper.decrypt(rawResponse.getKey3()));
        ret.setLicenseType(SecurityHelper.decrypt(rawResponse.getKey4()));
        ret.setMsg(rawResponse.getMsg());
        ret.setCode(rawResponse.getCode());
        ret.setStatus(rawResponse.getStatus());
        return ret;
    }


}
