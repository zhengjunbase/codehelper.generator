package com.ccnode.codegenerator.server.pojo;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:38
 */
public class RegisterRequest extends ServerRequest {

    String license;

    @Override
    public String getLicense() {
        return license;
    }

    @Override
    public void setLicense(String license) {
        this.license = license;
    }
}
