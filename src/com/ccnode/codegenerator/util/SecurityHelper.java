package com.ccnode.codegenerator.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/23 17:18
 */
public class SecurityHelper {

    private static String encryptKey = "fascias";

    @NotNull
    public static String encrypt( String encryptContent){
        String encrypt = SecurityUtil.AES.encrypt(encryptContent, encryptKey);
        if(StringUtils.isBlank(encrypt)){
            return StringUtils.EMPTY;
        }
        return encrypt;
    }

    @NotNull
    public static String encryptDate(@NotNull Date date){
        String timeStampStr = String.valueOf(date.getTime());
        String encrypt = SecurityUtil.AES.encrypt(timeStampStr, encryptKey);
        if(StringUtils.isBlank(encrypt)){
            return StringUtils.EMPTY;
        }
        return encrypt;
    }

    @NotNull
    public static String decrypt( String decryptContent){
        String decrypt = SecurityUtil.AES.decrypt(decryptContent, encryptKey);
        if(StringUtils.isBlank(decrypt)){
            return StringUtils.EMPTY;
        }
        return decrypt;
    }

    @Nullable
    public static Date decryptToDate( String decryptContent){
        String decryptDateStr = SecurityUtil.AES.decrypt(decryptContent, encryptKey);
        if(StringUtils.isBlank(decryptDateStr)){
            return null;
        }
        Long timeStamp = Long.valueOf(decryptDateStr);
        return new Date(timeStamp);
    }


    @Nullable
    public static String encrypt(String encryptKey, String encryptContent){
        return SecurityUtil.AES.encrypt(encryptContent,encryptKey);
    }

    @Nullable
    public static String encryptDate(String encryptKey,@NotNull Date date){
        String timeStampStr = String.valueOf(date.getTime());
        return SecurityUtil.AES.encrypt(timeStampStr, encryptKey);
    }

    @Nullable
    public static String decrypt(String decryptKey, String decryptContent){
        return SecurityUtil.AES.decrypt(decryptContent,decryptKey);
    }

    @Nullable
    public static Date decryptToDate(String decryptKey, String decryptContent){
        String decryptDateStr = SecurityUtil.AES.decrypt(decryptContent, decryptKey);
        if(StringUtils.isBlank(decryptDateStr)){
            return null;
        }
        Long timeStamp = Long.valueOf(decryptDateStr);
        return new Date(timeStamp);
    }

    public static void main(String[] args) {
        String key = encrypt("fascias",String.valueOf(1501683413030L));
        String qunar = decrypt("fascias", "gFXWrIcCdTUd86a3zzhHVw==");
        System.out.println(qunar);
    }
}
