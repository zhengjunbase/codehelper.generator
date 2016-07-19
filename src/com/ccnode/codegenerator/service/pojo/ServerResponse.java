package com.ccnode.codegenerator.service.pojo;

import com.ccnode.codegenerator.pojo.BaseResponse;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:41
 */
public class ServerResponse extends BaseResponse {

    ServerRequest request;
    String returnKey;
    String requestType;
    String updateMsg;
    String recommendMsg;
    String userType;
    Date expiredDate;

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

    public ServerRequest getRequest() {
        return request;
    }

    public void setRequest(ServerRequest request) {
        this.request = request;
    }

    public String getReturnKey() {
        return returnKey;
    }

    public void setReturnKey(String returnKey) {
        this.returnKey = returnKey;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }
}
