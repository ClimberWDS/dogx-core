package com.dogx.core.common.utils;

import com.dogx.core.base.model.RespResult;
import com.dogx.core.base.utils.CommonUtils;
import com.dogx.core.base.utils.JacksonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @Auther:hxl
 * @Date:2021/11/1-11-01 18:10
 * @Version:1.0
 */
@Slf4j
public class LogWrite extends Thread {
    private RespResult result;
    private long start;
    private long end;
    private String url;
    private String ip;
    private Map<String, String> paramMap;
    private String userId;
    public LogWrite(String url, String ip, Map<String, String> paramMap, RespResult result, long start, long end, String userId) {
        this.result = result;
        this.start = start;
        this.end = end;
        this.url = url;
        this.ip = ip;
        this.paramMap = paramMap;
        this.userId = userId;
    }

    public LogWrite(String url, String ip, Map<String, String> paramMap, RespResult result, long start, long end) {
        this.result = result;
        this.start = start;
        this.end = end;
        this.url = url;
        this.ip = ip;
        this.paramMap = paramMap;
        this.userId = userId;
    }

    @SneakyThrows
    @Override
    public void run() {
        if (paramMap != null) {
            // 图片指定字段 太长，不打印
            paramMap.remove("base64Images");
            // sm4key秘钥不打印
            paramMap.remove("sm4key");
        }
        if (CommonUtils.isEmpty(userId)) {
            log.info("{}|{}|{}|{}|{}|{}|{}|{}|{}", ip, url, start, end, end - start, result.getStatus(), result.getErrorCode(), result.getErrorMsg(), JacksonUtils.toJson(paramMap));
        } else {
            log.info("{}|{}|{}|{}|{}|{}|{}|{}|{}|{}", ip, url, start, end, end - start, result.getStatus(), result.getErrorCode(), result.getErrorMsg(), userId, JacksonUtils.toJson(paramMap));
        }
    }

}
