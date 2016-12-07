package com.ccnode.codegenerator.jpaparse;

/**
 * Created by bruce.ge on 2016/12/7.
 */
public class ReturnClassInfo {
    private String returnClassName;

    private String resultMap;

    private Boolean basicType = false;

    public String getReturnClassName() {
        return returnClassName;
    }

    public void setReturnClassName(String returnClassName) {
        this.returnClassName = returnClassName;
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    public Boolean getBasicType() {
        return basicType;
    }

    public void setBasicType(Boolean basicType) {
        this.basicType = basicType;
    }
}
