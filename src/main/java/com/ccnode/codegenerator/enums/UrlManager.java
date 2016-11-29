package com.ccnode.codegenerator.enums;

import com.ccnode.codegenerator.common.VersionManager;
import com.ccnode.codegenerator.storage.SettingService;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/08/31 16:39
 */
public class UrlManager {

    private static String GENERATOR_URL = "http://www.codehelper.me/generator/";
    private static String POST_URL = "http://www.codehelper.me/generator/post";
    private static String DONATE_CLICK_URL = "http://www.codehelper.me/generator" ;

    public static String getUrlSuffix (){
        return "?id=" + SettingService.getUUID() + "&version=" + VersionManager.getCurrentVersion();
    }

    public static String getGeneratorUrl() {
        return GENERATOR_URL + getUrlSuffix();
    }



    public static String getPostUrl() {
        return POST_URL + getUrlSuffix();
    }


    public static String getDonateClickUrl() {
        return DONATE_CLICK_URL + getUrlSuffix() + "#toc_4";
    }


}
