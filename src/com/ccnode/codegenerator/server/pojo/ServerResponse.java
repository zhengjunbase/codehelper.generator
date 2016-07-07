package com.ccnode.codegenerator.server.pojo;

import com.ccnode.codegenerator.enums.RequestType;
import com.ccnode.codegenerator.pojo.BaseResponse;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:41
 */
public class ServerResponse extends BaseResponse {

    ServerRequest request;
    String returnKey;
    RequestType requestType;
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

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}
