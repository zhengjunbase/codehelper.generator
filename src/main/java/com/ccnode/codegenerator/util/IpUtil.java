package com.ccnode.codegenerator.util;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/07/05 15:38
 */
public class IpUtil {

    public static String getMacAddress(){
        try{
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                builder.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return builder.toString();
        }catch(Exception e){
            return StringUtils.EMPTY;
        }
    }

}
