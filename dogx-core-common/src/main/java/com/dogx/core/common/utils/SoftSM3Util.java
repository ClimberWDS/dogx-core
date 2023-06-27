package com.dogx.core.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.Security;
import java.util.Arrays;

/**
 * @Auther:hxl
 * @Date:2021/9/1-09-01 15:34
 * @Version:1.0
 */
@Slf4j
public class SoftSM3Util {
    /***
     * 生成sm3哈希值
     * @param content
     * @return
     */
    private static final String ENCODING = "UTF-8";
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    static {
        Security.addProvider(new BouncyCastlePQCProvider());
    }

    public static byte[] hash(byte[] data) {
        SM3Digest digest = new SM3Digest();
        digest.update(data, 0, data.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }


    public static boolean verify(String srcStr, String sm3HexString) {
        boolean flag = false;
        try {
            byte[] srcData = srcStr.getBytes(ENCODING);
            byte[] sm3Hash = ByteUtils.fromHexString(sm3HexString);
            byte[] newHash = hash(srcData);
            if (Arrays.equals(newHash, sm3Hash)) {
                flag = true;
            }
        } catch (UnsupportedEncodingException e) {
            log.error("[SM3Util 异常] verify异常,原始数据:{},加密数据:{}", srcStr, sm3HexString, e);
        }
        return flag;
    }

    /**
     * 对数据流进行哈希
     * @param source will be closed
     * @return 十六进制哈希值字符串
     */
    public static byte[] encrypt(InputStream source) {
        try (InputStream inputStream = source) {
            MessageDigest instance = MessageDigest.getInstance("SM3", "BC");
            final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int read;
            while ((read = inputStream.read(buffer, 0, DEFAULT_BUFFER_SIZE)) > -1) {
                instance.update(buffer, 0, read);
            }
            return instance.digest();
        } catch (Exception e) {
            log.error("SM3哈希运算失败", e);
        }
        return null;
    }



}
