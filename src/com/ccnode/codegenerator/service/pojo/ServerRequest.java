package com.ccnode.codegenerator.service.pojo;

import com.ccnode.codegenerator.pojo.BaseRequest;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:22
 */
public class ServerRequest extends BaseRequest {

    String pluginVersion;
    String system;
    String ip;
    String macAddressList;
    String license;
    String requestType;
    String userType;

    public String getPluginVersion() {
        return pluginVersion;
    }

    public void setPluginVersion(String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMacAddressList() {
        return macAddressList;
    }

    public void setMacAddressList(String macAddressList) {
        this.macAddressList = macAddressList;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
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
}
