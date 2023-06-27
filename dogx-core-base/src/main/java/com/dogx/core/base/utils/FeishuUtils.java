package com.dogx.core.base.utils;

import com.dogx.core.base.model.ExtraCodeEnum;
import com.dogx.core.base.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mo
 * @Description 封装飞书的一些方法
 * @createTime 2022年07月20日 09:30
 */
@Slf4j
public class FeishuUtils {

    public static RespResult<Void> sendMsg(String url, String secret, String msg) {
        RespResult<?> genBody = genBody(secret, msg);
        if (!genBody.result()) {
            return (RespResult<Void>) genBody;
        }
        String body = (String) genBody.getData();
        String s = HttpUtils.doPost(url, body, Collections.emptyMap());
        return parseRes(s);
    }

    private static RespResult<?> genBody(String secret, String msg) {
        Map<String, Object> bodyMap = new HashMap<>(8);
        long ts = System.currentTimeMillis() / 1000;
        bodyMap.put("timestamp", ts);
        String sign = genSign(secret, ts);
        if (StringUtils.isBlank(sign)) {
            return RespResult.fail(ExtraCodeEnum.INNER_SERVER_ERROR.getCode(), "飞书机器人生成签名失败，请联系开发！");
        }
        bodyMap.put("sign", sign);
        bodyMap.put("msg_type", "text");
        Map<String, String> contentMap = new HashMap<>(2);
        contentMap.put("text", msg);
        bodyMap.put("content", contentMap);
        return RespResult.success(JacksonUtils.toJson(bodyMap));
    }

    private static String genSign(String secret, long ts) {
        String stringToSign = ts + "\n" + secret;
        try {
            //使用HmacSHA256算法计算签名
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] signData = mac.doFinal(new byte[]{});
            return Base64.toBase64String(signData);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("飞书机器人生成签名失败：{}", e.getMessage(), e);
            return null;
        }
    }

    private static RespResult<Void> parseRes(String res) {
        Map map = JacksonUtils.toObj(res, Map.class);
        Object statusCode = map.get("StatusCode");
        if (isSuccess(statusCode)) {
            return RespResult.success();
        } else {
            return RespResult.fail(ExtraCodeEnum.INNER_SERVER_ERROR.getCode(), "飞书机器人发送消息失败：" + res);
        }
    }

    private static boolean isSuccess(Object statusCode) {
        return Integer.valueOf(0).equals(statusCode);
    }

    public static void main(String[] args) {
        RespResult<Void> res = FeishuUtils.sendMsg("https://open.feishu.cn/open-apis/bot/v2/hook/3643f108-234b-45b5-a985-84931849bcc9", "mh8rl4rUDoM6t0pE8hEC2d", "各位早上好！");
        log.info("结果：{}", res);
    }

}
