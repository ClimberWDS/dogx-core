package com.dogx.core.base.utils;


import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 通过流方式加密
 * @author shawoo
 */
public class StreamHttp {
    private static Logger logger = LoggerFactory.getLogger(StreamHttp.class);

    public static int CONNECT_TIMEOUT = 5*1000;
    public static int READ_TIMEOUT = 30*1000;

    public static String doPost(String url,String userAgent,String authorization,String postData) throws Exception {
        return doPost(url,userAgent,authorization,postData.getBytes("UTF-8"));
    }

    public static String doPost(String url, String userAgent, String authorization, byte[] PostData) throws Exception {
        URL u = null;
        HttpURLConnection con = null;
        InputStream inputStream = null;
        long starttime = System.currentTimeMillis();
        //尝试发送请求
        try {
            u = new URL(url);
            con = (HttpURLConnection) u.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(CONNECT_TIMEOUT);
            con.setReadTimeout(READ_TIMEOUT);
            con.setRequestProperty("User-Agent", userAgent);
            con.setRequestProperty("Authorization", authorization);
            con.setRequestProperty("Content-Type", "application/json");
            OutputStream outStream = con.getOutputStream();
            outStream.write(PostData);
            outStream.flush();
            outStream.close();
            //读取返回内容
            inputStream = con.getInputStream();
            String s = IOUtils.toString(inputStream, "UTF-8");
            long endtime = System.currentTimeMillis();
            logger.info(String.format("请求%s，耗时%s",url,endtime-starttime));
            return s;
        } catch (Exception e) {
            throw e;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
