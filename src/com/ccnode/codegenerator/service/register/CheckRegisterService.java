package com.ccnode.codegenerator.service.register;

import com.ccnode.codegenerator.storage.SettingService;
import com.ccnode.codegenerator.util.HttpUtil;
import com.ccnode.codegenerator.util.LoggerWrapper;
import com.ccnode.codegenerator.util.SecurityHelper;
import org.slf4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/23 17:51
 */
public class CheckRegisterService {

    private final static Logger LOGGER = LoggerWrapper.getLogger(CheckRegisterService.class);

    public static Boolean checkFromLocal(){
        List<String> keyList = SettingService.getInstance().getState().getKeyList();
        for (String s : keyList) {
            Date date = SecurityHelper.decryptToDate(s);
            if(date == null){
                continue;
            }
            if(date.compareTo(new Date()) > 0){
                return true;
            }
        }
        return false;
    }

    // TODO: 7/23/16
    public static Boolean checkOnline(){
        try{
            String s = HttpUtil.postJson("", SettingService.getInstance().getState());
            if(s.contains("SUCCESS")){
                return true;
            }else{
                return false;
            }

        }catch(Throwable e){
            return false;
        }


    }
}
