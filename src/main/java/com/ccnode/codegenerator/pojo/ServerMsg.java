package com.ccnode.codegenerator.pojo;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/09/03 10:11
 */
public class ServerMsg {

    Boolean hasServerMsg;
    String content;
    String title;
    String buttonStr;
    String buttonUrl;

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

    public Boolean getHasServerMsg() {
        return hasServerMsg;
    }

    public void setHasServerMsg(Boolean hasServerMsg) {
        this.hasServerMsg = hasServerMsg;
    }
}
