package com.ccnode.codegenerator.storage;

import com.ccnode.codegenerator.common.VersionManager;
import com.ccnode.codegenerator.enums.UrlManager;
import com.ccnode.codegenerator.service.SendToServerService;
import com.ccnode.codegenerator.util.DateUtil;
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

    public static String getUUID(){
        return getInstance().getState().getUuid();
    }

    public static SettingDto getSetting(){
        return getInstance().getState();
    }


    @NotNull
    @Override
    public SettingDto getState() {
        if(settingDto == null){
            settingDto = new SettingDto();
            settingDto.setUuid(UUID.randomUUID().toString());
            settingDto.setVersion(VersionManager.getCurrentVersion());
            if(settingDto.getInstalledDate() == null){
                settingDto.setInstalledDate(new Date());
            }
        }else{
            if(settingDto.getUpdateDate() == null && !VersionManager.CURRENT_VERSION.equalsIgnoreCase(settingDto.getVersion())){
                settingDto.setOldVersion(settingDto.getVersion());
                settingDto.setVersion(VersionManager.CURRENT_VERSION);
                settingDto.setUpdateDate(new Date());
            }
        }
        if(StringUtils.isBlank(settingDto.getUuid())){
            settingDto.setUuid(UUID.randomUUID().toString());
        }
        return settingDto;
    }


    @Override
    public void loadState(SettingDto o) {
        XmlSerializerUtil.copyBean(o, getState());
    }

    public static void setDonated(){
        getInstance().getState().setDonatedDate(new Date());
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

    public static boolean showDonateBtn(){
//        Integer count = getSetting().getCount();
//        if(isDonated()){
//            return false;
//        }
//        if(count == null count > 5 && count % 7 == 0){
//            return true;
//        }
        return false;
    }

    public static boolean isDonated() {
        return getInstance().getState().getDonatedDate() != null && DateUtil.getDayBetween(getInstance().getState().getDonatedDate(), new Date()) < 365;
    }

    public static String getOldVersion(){
        String oldVersion = getInstance().getState().getVersion();
        if(StringUtils.isBlank(oldVersion)){
            return "no_version";
        }
        return oldVersion;
    }

    public static void updateLastRunTime(){
        getSetting().setLastRunTime(new Date());
    }

    public static int getSecondAfterLastRun(){
        long l = new Date().getTime() - getSetting().getLastRunTime().getTime() /1000L;
        return (int) l;
    }

}