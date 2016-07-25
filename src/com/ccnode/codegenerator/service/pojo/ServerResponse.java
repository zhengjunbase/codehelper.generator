package com.ccnode.codegenerator.service.pojo;

import com.ccnode.codegenerator.pojo.BaseResponse;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:41
 */
public class ServerResponse extends BaseResponse {

    String requestType;
    String updateMsg;
    String recommendMsg;

    public String getUpdateMsg() {
        return updateMsg;
    }

    public void setUpdateMsg(String updateMsg) {
        this.updateMsg = updateMsg;
    }

    public String getRecommendMsg() {
        return recommendMsg;
    }

    public void setRecommendMsg(String recommendMsg) {
        this.recommendMsg = recommendMsg;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

}
