package com.ccnode.codegenerator.pojo;

import com.ccnode.codegenerator.util.SecurityHelper;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/08/02 09:44
 */
public class RegisterRawResponse extends BaseResponse {

//    String license;
    String key1;
//    Date expireDate;
    String key2;
//    String licenseStatus;
    String key3;
//    String licenseType;
    String key4;

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getKey3() {
        return key3;
    }

    public void setKey3(String key3) {
        this.key3 = key3;
    }

    public String getKey4() {
        return key4;
    }

    public void setKey4(String key4) {
        this.key4 = key4;
    }

    public static void main(String[] args) {
        Date date = SecurityHelper.decryptToDate("bfWsh6Y9JSdxH/XH8D5KVA==");
        System.out.println(date);
        String s = SecurityHelper.encryptDate(date);
        System.out.println(s);
    }
}
