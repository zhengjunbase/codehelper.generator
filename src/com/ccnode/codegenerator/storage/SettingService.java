package com.ccnode.codegenerator.storage;

import com.ccnode.codegenerator.util.PojoUtil;
import com.ccnode.codegenerator.util.SecurityHelper;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        if(settingDto == null){
            settingDto = new SettingDto();
            settingDto.setUuid(UUID.randomUUID().toString());
        }
        if(StringUtils.isBlank(settingDto.getUuid())){
            settingDto.setUuid(UUID.randomUUID().toString());
        }
        return settingDto;
    }

    public static Boolean checkSuccess(){
        SettingService instance = getInstance();
        if(StringUtils.isBlank(instance.getState().getrKey())){
            return true;
        }
        return "423".equals(instance.getState().getrKey());
    }

    public static void setCheckFailure(){
        getInstance().getState().setrKey("413");
    }


    public static void setCheckSuccess(){
        getInstance().getState().setrKey("423");
    }

    @Override
    public void loadState(SettingDto o) {
        XmlSerializerUtil.copyBean(o, getState());
    }

    public void setSettingDto(SettingDto settingDto) {
        this.settingDto = settingDto;
    }

    public static Boolean notExpired(String eKey) {
        Date date = SecurityHelper.decryptToDate(eKey);
        if (date == null || new Date().compareTo(date) > 0) {
            return false;
        } else {
            return true;
        }
    }
    public static Boolean canUsePremium(){
        return checkSuccess() && notExpired(getInstance().getState().geteKey());
    }

}