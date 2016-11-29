package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.common.VersionManager;
import com.ccnode.codegenerator.service.pojo.ServerRequest;
import com.ccnode.codegenerator.storage.SettingService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/16 22:00
 */
public class ServerRequestHelper {


    public static <T extends ServerRequest> T fillCommonField(T request){

        request.setPluginVersion(VersionManager.getCurrentVersion());
        request.setIp(getIpAddress());
        request.setOsName(System.getProperty("os.name"));
        request.setOsVersion(System.getProperty("os.version"));
        request.setUuid(SettingService.getUUID());
        return request;
    }


    public static String getIpAddress() {
        long startTime = System.currentTimeMillis();
        try{
            return  InetAddress.getLocalHost().getHostAddress();
        }catch(Throwable e){
            return "NO_IP_ADDRESS";
        }

    }

}
