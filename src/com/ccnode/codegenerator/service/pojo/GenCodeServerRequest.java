package com.ccnode.codegenerator.service.pojo;

import com.ccnode.codegenerator.pojo.ChangeInfo;
import com.ccnode.codegenerator.storage.SettingDto;
import com.google.common.base.Throwables;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/09/17 18:27
 */
public class GenCodeServerRequest extends ServerRequest {

    private static final long serialVersionUID = -8687380055394742770L;

    List<ChangeInfo> changeInfos;
    SettingDto settingDto;
    List<String> stackTraceMsg;

    public List<ChangeInfo> getChangeInfos() {
        return changeInfos;
    }

    public void setChangeInfos(List<ChangeInfo> changeInfos) {
        this.changeInfos = changeInfos;
    }

    public List<String> getStackTraceMsg() {
        return stackTraceMsg;
    }

    public void setStackTraceMsg(List<String> stackTraceMsg) {
        this.stackTraceMsg = stackTraceMsg;
    }

    public SettingDto getSettingDto() {
        return settingDto;
    }

    public void setSettingDto(SettingDto settingDto) {
        this.settingDto = settingDto;
    }
}
