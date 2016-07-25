package com.ccnode.codegenerator.pojoHelper;

import com.ccnode.codegenerator.common.VersionManager;
import com.ccnode.codegenerator.service.pojo.ServerRequest;
import com.google.common.collect.Lists;

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

    private static String URL = "www.codehelper.me/generator";

    public static <T extends ServerRequest> T fillCommonField(T request){

        request.setVersion(VersionManager.getCurrentVersion());
        request.setIp(getIpAddress());
        request.setMacAddressList(getMacAddress());
        return request;
    }

    public static List<String> getMacAddress() {
        List<String> retList = Lists.newArrayList();
        try{
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
             for (; networks.hasMoreElements();){
                byte[] mac = networks.nextElement().getHardwareAddress();
                if(mac == null){
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    builder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                }
                 retList.add(builder.toString());
             }
        }catch(Throwable e){

        }
        return retList;
    }

    public static String getIpAddress() {
        long startTime = System.currentTimeMillis();
        try{
            return  InetAddress.getLocalHost().getHostAddress();
        }catch(Throwable e){
            return "NO_IP_ADDRESS";
        }

    }

    public static void main(String[] args) {
        System.out.println(getMacAddress());
        System.out.println(getIpAddress());
    }
}
