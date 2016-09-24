package com.ccnode.codegenerator.service.pojo;

import com.ccnode.codegenerator.pojo.ChangeInfo;
import com.google.common.base.Throwables;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/09/17 18:27
 */
public class SendToServerRequest extends ServerRequest {

    private static final long serialVersionUID = -8687380055394742770L;

    List<ChangeInfo> changeInfos;
    List<String> errorMsg;
    List<String> stackTraceMsg;

    public List<ChangeInfo> getChangeInfos() {
        return changeInfos;
    }

    public void setChangeInfos(List<ChangeInfo> changeInfos) {
        this.changeInfos = changeInfos;
    }

    public List<String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(List<String> errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<String> getStackTraceMsg() {
        return stackTraceMsg;
    }

    public void setStackTraceMsg(List<String> stackTraceMsg) {
        this.stackTraceMsg = stackTraceMsg;
    }
}
