package com.ccnode.codegenerator.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created on 3/24/16.
 */
public abstract class BaseRequest implements Serializable {

    private static final long serialVersionUID = 790960324431455971L;
    /**
     * 版本号
     */
    private String version;
    /**
     * 业务线id
     */
    private String busiTypeId;
    /**
     * 调用系统标识
     */
    private String consumerKey;
    /**
     * 请求时间
     */
    private Date requestTime;
    /**
     * 请求号
     */
    private String requestNo;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBusiTypeId() {
        return busiTypeId;
    }

    public void setBusiTypeId(String busiTypeId) {
        this.busiTypeId = busiTypeId;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }
}
