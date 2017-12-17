package com.ccnode.codegenerator.pojo;

import org.apache.commons.lang3.StringUtils;

/**
 * Created on 4/1/16.
 */
public class BaseResponse  {
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
    private String errorMessage;

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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public <T extends BaseResponse> T success() {
        this.status = SUCCESS;
        this.code = "";
        this.errorMessage = "SUCCESS";
        return (T) this;
    }

    public <T extends BaseResponse> T accept() {
        this.status = ACCEPT;
        this.code = "";
        this.errorMessage = "请求处理中";
        return (T) this;
    }

    public <T extends BaseResponse> T accept(String msg) {
        this.status = ACCEPT;
        this.code = "";
        this.errorMessage = msg;
        return (T) this;
    }

    public <T extends BaseResponse> T failure(String code, String errMsg) {
        this.status = FAILURE;
        this.code = code;
        this.errorMessage = errMsg;
        return (T) this;
    }

    public <T extends BaseResponse> T failure(String errMsg) {
        this.status = FAILURE;
        this.code = StringUtils.EMPTY;
        this.errorMessage = errMsg;
        return (T) this;
    }

    public <T extends BaseResponse> T failure(String errMsg,Throwable throwable) {
        this.status = FAILURE;
        this.code = StringUtils.EMPTY;
        this.errorMessage = errMsg;
        this.throwable = throwable;
        return (T) this;
    }

    public <T extends BaseResponse> T failure(RetStatus retStatus) {
        this.status = FAILURE;
        this.code = retStatus.getCode().toString();
        this.errorMessage = retStatus.getDesc();
        return (T) this;
//        throw new BizException();
    }

    public <T extends BaseResponse> T failure(RetStatus retStatus, String errorMsg) {
        this.status = FAILURE;
        this.code = retStatus.getCode().toString();
        this.errorMessage = errorMsg;
        return (T) this;
    }

    public boolean isSuccess() {
        return SUCCESS.equalsIgnoreCase(status);
    }

    public boolean isAccepted() {
        return ACCEPT.equals(status);
    }

    public boolean isFailure() {
        return FAILURE.equals(status);
    }
}
