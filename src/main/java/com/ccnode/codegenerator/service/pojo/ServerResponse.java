package com.ccnode.codegenerator.service.pojo;

import com.ccnode.codegenerator.pojo.BaseResponse;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:41
 */
public class ServerResponse extends BaseResponse {

    private static final long serialVersionUID = 4421391031885485173L;

    Boolean hasServerMsg;
    String content;
    String title;
    String buttonStr;
    String buttonUrl;
    String status;
    Integer msgId;

    public Boolean getHasServerMsg() {
        return hasServerMsg;
    }

    public void setHasServerMsg(Boolean hasServerMsg) {
        this.hasServerMsg = hasServerMsg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getButtonStr() {
        return buttonStr;
    }

    public void setButtonStr(String buttonStr) {
        this.buttonStr = buttonStr;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }
}
