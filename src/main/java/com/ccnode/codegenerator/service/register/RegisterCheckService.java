package com.ccnode.codegenerator.service.register;

import com.ccnode.codegenerator.enums.UrlManager;
import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.HttpUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.SecurityHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/23 17:51
 */
public class RegisterCheckService {

    private final static Logger LOGGER = LoggerWrapper.getLogger(RegisterCheckService.class);


//    public static Boolean checkAll(){
//        return checkFromLocal() && checkOnline();
//    }
//
//    public static Boolean checkFromLocal(){
//        String eKey = SettingService.getInstance().getState().geteKey();
//        if(StringUtils.isBlank(eKey)){
//            return false;
//        }
//        Date date = SecurityHelper.decryptToDate(eKey);
//        if(date == null || date.compareTo(new Date()) < 0){
//            return false;
//        }
//        return true;
//    }
//
//    public static Boolean checkOnline(){
//        try{
//            String s = HttpUtil.postJsonEncrypt(UrlManager.REGISTER_CHECK_URL +"?id=" + SettingService.getUUID(), SettingService.getInstance().getState());
//            if(s.contains("FAILURE")){
//                SettingService.setCheckFailure();
//                return false;
//            }else{
//                SettingService.setCheckSuccess();
//                return true;
//            }
//        }catch(Throwable e){
//            return true;
//        }
//    }

}
