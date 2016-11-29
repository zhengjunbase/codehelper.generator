package com.ccnode.codegenerator.service.pojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 16:38
 */
public class RegisterRequest extends ServerRequest {

    String license;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
