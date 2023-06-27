package com.dogx.core.common.utils;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.util.ECUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author shawoo
 */
public class PublicKeyUtils {

    private static Logger log = LoggerFactory.getLogger(PublicKeyUtils.class);

    static {
        Security.removeProvider("SunEC");
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String encryptKi(String content, String certContent) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, InvalidCipherTextException, InvalidKeySpecException {
        ByteArrayInputStream stringStream = new ByteArrayInputStream(Base64Utils.decodeFromString(certContent));
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(stringStream);
        BCECPublicKey publicKey = (BCECPublicKey) cert.getPublicKey();
        CipherParameters pubKeyParameters = new ParametersWithRandom(publicKeyToParams("EC", publicKey.getEncoded()));
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        engine.init(true, pubKeyParameters);
        byte[] data = engine.processBlock(content.getBytes(), 0, content.length());
        System.out.println(Base64Utils.encodeToString(data));
        return Base64Utils.encodeToString(data);
    }

    public static X509Certificate getPublicKeyByCert(File file) {
        CertificateFactory cf;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(fileInputStream);
        } catch (CertificateException | IOException e) {
            log.error("[PublicKeyUtils 异常] getPublicKeyByCert异常,公钥是否为空:{}", file == null, e);
        }
        return null;
    }


    public static ECPublicKeyParameters publicKeyToParams(String algorithm, byte[] key) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {
        PublicKey publicKey = generatePublicKey(algorithm, key);
        return (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(publicKey);
    }

    public static PublicKey generatePublicKey(String algorithm, byte[] key) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new X509EncodedKeySpec(key);
        return getKeyFactory(algorithm).generatePublic(keySpec);
    }


    /**
     * 获取{@link KeyFactory}
     *
     * @param algorithm 非对称加密算法
     * @return {@link KeyFactory}
     */
    private static KeyFactory getKeyFactory(String algorithm) throws NoSuchAlgorithmException {
        final Provider provider = new BouncyCastleProvider();
        return KeyFactory.getInstance(algorithm, provider);
    }
}
