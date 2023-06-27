package com.dogx.core.base.utils;

import com.dogx.core.base.interceptor.HttpLogInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther:hxl
 * @Date:2021/8/17-08-17 9:21
 * @Version:1.0
 */
public class HttpUtils {
    public static long CONNECT_TIMEOUT = 10;
    public static long READ_TIMEOUT = 60;
    public static long WRITE_TIMEOUT = 60;

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    public static String doGet(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    //连接超时
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    //读取超时
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLogInterceptor())
                    .build(); //写超时
            if (paramMap != null && paramMap.size() > 0) {
                String paramStr = "";
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    paramStr += entry.getKey() + "=" + entry.getValue() + "&";
                }
                if (paramStr.length() > 1) {
                    paramStr = paramStr.substring(0, paramStr.length() - 1);
                    url = url + "?" + paramStr;
                }

            }
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            String traceId = MDC.get("traceId");
            String spanId = MDC.get("spanId");
            if (StringUtils.isNotBlank(traceId)) {
                builder.addHeader("X-B3-TraceId", traceId);
            }
            if (StringUtils.isNotBlank(spanId)) {
                builder.addHeader("X-B3-SpanId", spanId);
            }
            if (headerMap != null && headerMap.size() > 0) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            final Request request = builder.get().build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            logger.error("OKHttp3通过Get请求接口失败", e);
        }
        return null;
    }

    public static String doGet(String url) {
        return doGet(url, null, null);
    }

    public static String doPost(String url) {
        return doPost(url, new HashMap<String, String>(), null);
    }

    public static String doPost(String url, Map<String, String> paramMap, Map<String, String> headerMap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String paramStr = objectMapper.writeValueAsString(paramMap);
            return doPost(url, paramStr, headerMap);
        } catch (Exception e) {
            logger.error("http请求错误[" + url + "]", e);
            throw new RuntimeException("http请求错误", e);
        }
    }

    public static String doPostOnObject(String url, Map<String, Object> paramMap, Map<String, String> headerMap) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String paramStr = objectMapper.writeValueAsString(paramMap);
            return doPost(url, paramStr, headerMap);
        } catch (Exception e) {
            logger.error("http请求错误[" + url + "]", e);
            throw new RuntimeException("http请求错误", e);
        }
    }

    public static String doPost(String url, String paramStr, Map<String, String> headerMap) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    //连接超时
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    //读取超时
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(new HttpLogInterceptor())
                    .build(); //写超时
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.addHeader("Content-Type", "application/json");
            String traceId = MDC.get("traceId");
            String spanId = MDC.get("spanId");
            if (StringUtils.isNotBlank(traceId)) {
                builder.addHeader("X-B3-TraceId", traceId);
            }
            if (StringUtils.isNotBlank(spanId)) {
                builder.addHeader("X-B3-SpanId", spanId);
            }
            if (headerMap != null && headerMap.size() > 0) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            builder.post(RequestBody.create(mediaType, paramStr));
            final Request request = builder.build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            logger.error("OKHttp3通过Post请求接口失败,url:" + url, e);
            throw new RuntimeException("请求外部服务失败", e);
        }
    }

    public static String doPost(String url, byte[] paramStr, Map<String, String> headerMap) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    //连接超时
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    //读取超时
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    //.addInterceptor(new HttpLogInterceptor())
                    .build(); //写超时
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            builder.addHeader("Content-Type", "application/json");
            String traceId = MDC.get("traceId");
            String spanId = MDC.get("spanId");
            if (StringUtils.isNotBlank(traceId)) {
                builder.addHeader("X-B3-TraceId", traceId);
            }
            if (StringUtils.isNotBlank(spanId)) {
                builder.addHeader("X-B3-SpanId", spanId);
            }
            if (headerMap != null && headerMap.size() > 0) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    builder.addHeader(entry.getKey(), entry.getValue());
                }
            }
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            builder.post(RequestBody.create(mediaType, paramStr));
            final Request request = builder.build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            logger.error("OKHttp3通过Post请求接口失败2", e);
            throw new RuntimeException("请求外部服务失败", e);
        }
    }
}
