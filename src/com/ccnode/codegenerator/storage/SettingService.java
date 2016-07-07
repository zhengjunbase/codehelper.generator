package com.ccnode.codegenerator.storage;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.spring.model.utils.SpringBeanUtils;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 14:26
 */

@State(name="SettingService", storages={@Storage(id="other", file="$APP_CONFIG$/mybatis.xml")})
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
        }
        return settingDto;
    }


    @Override
    public void loadState(SettingDto o) {
        XmlSerializerUtil.copyBean(o, this);
    }

    public void setSettingDto(SettingDto settingDto) {
        this.settingDto = settingDto;
    }
}