package com.dogx.core.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Auther:hxl
 * @Date:2021/11/1-11-01 16:42
 * @Version:1.0
 */
@Slf4j
public class IpUtils {
    /***
     * 获取主机ip地址
     * @return
     */
    public static String getHostIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    String hostAddress = inetAddress.getHostAddress();
                    if(!inetAddress.isLoopbackAddress() && hostAddress.indexOf(":")==-1) {
                        return hostAddress;
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取主机ip错误：", e);
        }
        return null;
    }

    /***
     * 获取客户端的ip
     * @param request
     * @return
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
