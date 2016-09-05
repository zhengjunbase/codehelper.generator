package com.ccnode.codegenerator.storage;

import com.ccnode.codegenerator.pojoHelper.GenCodeResponseHelper;
import com.ccnode.codegenerator.util.PojoUtil;
import com.ccnode.codegenerator.util.SecurityHelper;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 14:26
 */

@State(name="SettingService", storages={@Storage(id="other", file="$APP_CONFIG$/codeHelper.xml")})
public class SettingService implements PersistentStateComponent<SettingDto> {

     SettingDto settingDto;

    public static SettingService getInstance(){
       return ServiceManager.getService(SettingService.class);
    }

    @NotNull
    @Override
    public SettingDto getState() {
        if(settingDto == null || GenCodeResponseHelper.isRegisterDebug()){
            settingDto = new SettingDto();
        }
        return settingDto;
    }


    @Override
    public void loadState(SettingDto o) {
        XmlSerializerUtil.copyBean(o, getState());
    }

    public void setSettingDto(SettingDto settingDto) {
        this.settingDto = settingDto;
    }

    public Boolean canUsePremium(){
        List<String> keyList = getState().getKeyList();
        keyList = PojoUtil.avoidEmptyList(keyList);
        Boolean expired = true;
        for (String key : keyList) {
            Date date = SecurityHelper.decryptToDate(key);
            if(date == null || new Date().compareTo(date) > 0){
                continue;
            }else{
                expired = false;
            }
        }
        if(expired){
            return false;
        }else{
            return true;
        }
    }

}