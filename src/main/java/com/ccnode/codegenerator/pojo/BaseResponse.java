package com.ccnode.codegenerator.pojo;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Created on 4/1/16.
 */
public class BaseResponse implements Serializable {
    /**
     * 0 - 只接受请求，为处理中
     */
    public static final String ACCEPT = "ACCEPT";
    /**
     * 1 - 处理成功
     */
    public static final String SUCCESS = "SUCCESS";
    /**
     * 2 - 处理失败
     */
    public static final String FAILURE = "FAILURE";

    private static final long serialVersionUID = -3041437055079912036L;

    /**
     * 状态
     */
    private String status;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String msg;

    private Throwable throwable;

    public BaseResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public <T extends BaseResponse> T success() {
        this.status = SUCCESS;
        this.code = "";
        this.msg = "SUCCESS";
        return (T) this;
    }

    public <T extends BaseResponse> T accept() {
        this.status = ACCEPT;
        this.code = "";
        this.msg = "请求处理中";
        return (T) this;
    }

    public <T extends BaseResponse> T accept(String msg) {
        this.status = ACCEPT;
        this.code = "";
        this.msg = msg;
        return (T) this;
    }

    public <T extends BaseResponse> T failure(String code, String errMsg) {
        this.status = FAILURE;
        this.code = code;
        this.msg = errMsg;
        return (T) this;
    }

    public <T extends BaseResponse> T failure(String errMsg) {
        this.status = FAILURE;
        this.code = StringUtils.EMPTY;
        this.msg = errMsg;
        return (T) this;
    }

    public <T extends BaseResponse> T failure(String errMsg,Throwable throwable) {
        this.status = FAILURE;
        this.code = StringUtils.EMPTY;
        this.msg = errMsg;
        this.throwable = throwable;
        return (T) this;
    }

    public <T extends BaseResponse> T failure(RetStatus retStatus) {
        this.status = FAILURE;
        this.code = retStatus.getCode().toString();
        this.msg = retStatus.getDesc();
        return (T) this;
//        throw new BizException();
    }

    public <T extends BaseResponse> T failure(RetStatus retStatus, String errorMsg) {
        this.status = FAILURE;
        this.code = retStatus.getCode().toString();
        this.msg = errorMsg;
        return (T) this;
    }

    public boolean checkSuccess() {
        return SUCCESS.equalsIgnoreCase(status);
    }

    public boolean checkAccept() {
        return ACCEPT.equals(status);
    }

    public boolean checkFailure() {
        return FAILURE.equals(status);
    }
}
