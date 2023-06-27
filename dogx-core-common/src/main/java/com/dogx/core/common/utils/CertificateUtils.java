package com.dogx.core.common.utils;


import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class CertificateUtils {

    private static Signature signature;

    private static final String SM2SIGN_WITH_SM3 = "SM3withSM2";

    static {
        try {
            signature = Signature.getInstance(
                    SM2SIGN_WITH_SM3,
                    new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static synchronized byte[] sign(String src, PrivateKey privateKey) throws InvalidKeyException, SignatureException {
        signature.initSign(privateKey);
        byte[] plainText = src.getBytes(StandardCharsets.UTF_8);
        signature.update(plainText);
        return signature.sign();
    }

    public static synchronized boolean verify(String plainText, byte[] signText, PublicKey publicKey) throws InvalidKeyException, SignatureException {
        signature.initVerify(publicKey);
        signature.update(plainText.getBytes());
        return signature.verify(signText);
    }


    public static synchronized byte[] sign(byte[] plainText, PrivateKey privateKey) throws InvalidKeyException, SignatureException {
        signature.initSign(privateKey);
        signature.update(plainText);
        return signature.sign();
    }

    public static synchronized boolean  verify(byte[] originalValue, byte[] signText, byte[] publicKey) throws InvalidKeyException, SignatureException, InvalidKeySpecException, NoSuchAlgorithmException {
        signature.initVerify(getBCECPublicKey(publicKey));
        signature.update(originalValue);
        return signature.verify(signText);
    }


    /**
     * 根据字符公钥转化为公钥对象
     *
     * @param pubKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getBCECPublicKey(byte[] pubKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 获取SM2相关参数
        X9ECParameters parameters = GMNamedCurves.getByName("sm2p256v1");
        // 椭圆曲线参数规格
        org.bouncycastle.jce.spec.ECParameterSpec ecParameterSpec = new org.bouncycastle.jce.spec.ECParameterSpec(parameters.getCurve(), parameters.getG(), parameters.getN(), parameters.getH());
        // 将公钥HEX字符串转换为椭圆曲线对应的点
        org.bouncycastle.math.ec.ECPoint ecPoint = parameters.getCurve().decodePoint(pubKey);
        // 将私钥HEX字符串转换为X值
        // 获取椭圆曲线KEY生成器
        KeyFactory keyFactory = KeyFactory.getInstance("EC", new BouncyCastleProvider());
        // 将椭圆曲线点转为公钥KEY对象
        BCECPublicKey publicKey = (BCECPublicKey) keyFactory.generatePublic(new org.bouncycastle.jce.spec.ECPublicKeySpec(ecPoint, ecParameterSpec));
        return publicKey;
    }
}
