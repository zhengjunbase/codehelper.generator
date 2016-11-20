package com.ccnode.codegenerator.service.pojo;

import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/22 22:10
 */
public class RegisterResponse extends ServerResponse {

    String license;

    Date expireDate;
    String licenseStatus;
    String licenseType;

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(@Nullable Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getLicenseStatus() {
        return licenseStatus;
    }

    public void setLicenseStatus(String licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }
}
