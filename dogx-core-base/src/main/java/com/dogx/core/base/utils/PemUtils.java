package com.dogx.core.base.utils;


import com.dogx.core.base.constant.ConfigConsts;
import com.dogx.core.base.model.CertInfoEntity;
import com.dogx.core.base.model.PemEntity;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/***
 * 读取pem文件
 */
public class PemUtils {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /***
     * 读取pem文件内容
     * @param filePath
     * @return
     * @throws IOException
     */
    public static PemEntity parsePem(String filePath) throws IOException {
        String result = FileUtils.readFileToString(new File(filePath), "UTF-8");
        return parsePemWithContent(result);
    }

    public static PemEntity parsePemWithContent(String result) {
        PemEntity pemEntity = new PemEntity();
        String selfSignStartFlag = "-----BEGIN MY SIGN PASSPORT-----";
        String selfSignEndFlag = "-----END MY SIGN PASSPORT-----";
        String selfEncryptStartFlag = "-----BEGIN MY ENCIPHER PASSPORT-----";
        String selfEncryptEndFlag = "-----END MY ENCIPHER PASSPORT-----";

        String centerSignStartFlag = "-----BEGIN CENTER SIGN PASSPORT-----";
        String centerSignEndFlag = "-----END CENTER SIGN PASSPORT-----";
        String centerEncryptStartFlag = "-----BEGIN CENTER ENCIPHER PASSPORT-----";
        String centerEncryptEndFlag = "-----END CENTER ENCIPHER PASSPORT-----";

        if (result.contains(selfSignStartFlag)) {
            pemEntity.setSelfSignCert(result.substring(result.indexOf(selfSignStartFlag), result.lastIndexOf(selfSignEndFlag) + selfSignEndFlag.length()));
            pemEntity.setSelfEncryptCert(result.substring(result.indexOf(selfEncryptStartFlag), result.lastIndexOf(selfEncryptEndFlag) + selfEncryptEndFlag.length()));
        }
        if (result.contains(centerSignStartFlag)) {
            pemEntity.setCenterSignCert(result.substring(result.indexOf(centerSignStartFlag), result.lastIndexOf(centerSignEndFlag) + centerSignEndFlag.length()));
            pemEntity.setCenterEncryptCert(result.substring(result.indexOf(centerEncryptStartFlag), result.lastIndexOf(centerEncryptEndFlag) + centerEncryptEndFlag.length()));
        }
        return pemEntity;
    }

    /***
     * 解析pem文件内容并读取证书内容
     * @param entity
     * @return
     * @throws CertificateException
     */
    public static CertInfoEntity getCertInfo(PemEntity entity) throws CertificateException, IOException {
        CertInfoEntity certInfoEntity = new CertInfoEntity();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Security.removeProvider("SunEC");
        InputStream encyptInputStream = IOUtils.toInputStream(entity.getSelfEncryptCert(), ConfigConsts.ENCODING);
        InputStream signInputStream = IOUtils.toInputStream(entity.getSelfSignCert(), ConfigConsts.ENCODING);
        X509Certificate cert = (X509Certificate) cf.generateCertificate(encyptInputStream);
        X509Certificate signCert = (X509Certificate) cf.generateCertificate(signInputStream);
        certInfoEntity.setVersion(cert.getVersion());
        certInfoEntity.setCertInfo(Base64.getEncoder().encodeToString(cert.getEncoded()));
        certInfoEntity.setPublicKey(Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded()));
        certInfoEntity.setSignKey(Base64.getEncoder().encodeToString(signCert.getPublicKey().getEncoded()));
        certInfoEntity.setSubject(cert.getSubjectDN().getName());
        certInfoEntity.setIssuer(cert.getIssuerDN().getName());
        certInfoEntity.setSerialNo(cert.getSerialNumber().longValue());
        certInfoEntity.setSigAlgName(cert.getSigAlgName());
        certInfoEntity.setSigAlgOid(cert.getSigAlgOID());
        certInfoEntity.setSignature(ByteUtils.toHexString(cert.getSignature()));
        certInfoEntity.setEffectiveTime(cert.getNotBefore());
        certInfoEntity.setExpireTime(cert.getNotAfter());
        certInfoEntity.setTBSCertificate(ByteUtils.toHexString(cert.getTBSCertificate()));
        return certInfoEntity;
    }

    public static CertInfoEntity getCertInfo(String certStr) throws CertificateException, IOException {
        CertInfoEntity certInfoEntity = new CertInfoEntity();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Security.removeProvider("SunEC");
        InputStream encyptInputStream = IOUtils.toInputStream(certStr, ConfigConsts.ENCODING);
        X509Certificate cert = (X509Certificate) cf.generateCertificate(encyptInputStream);
        certInfoEntity.setVersion(cert.getVersion());
        certInfoEntity.setCertInfo(Base64.getEncoder().encodeToString(cert.getEncoded()));
        certInfoEntity.setPublicKey(Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded()));
        certInfoEntity.setSubject(cert.getSubjectDN().getName());
        certInfoEntity.setIssuer(cert.getIssuerDN().getName());
        certInfoEntity.setSerialNo(cert.getSerialNumber().longValue());
        certInfoEntity.setSigAlgName(cert.getSigAlgName());
        certInfoEntity.setSigAlgOid(cert.getSigAlgOID());
        certInfoEntity.setSignature(ByteUtils.toHexString(cert.getSignature()));
        certInfoEntity.setEffectiveTime(cert.getNotBefore());
        certInfoEntity.setExpireTime(cert.getNotAfter());
        certInfoEntity.setTBSCertificate(ByteUtils.toHexString(cert.getTBSCertificate()));
        return certInfoEntity;
    }

    public static CertInfoEntity getCertInfoBySwxaJCE(String certStr) throws CertificateException, NoSuchProviderException, IOException {
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509", "SwxaJCE");
        InputStream encyptInputStream = IOUtils.toInputStream(certStr, ConfigConsts.ENCODING);
        X509Certificate cert = (X509Certificate) certFactory.generateCertificate(encyptInputStream);
        CertInfoEntity certInfoEntity = new CertInfoEntity();
        certInfoEntity.setVersion(cert.getVersion());
        certInfoEntity.setCertInfo(Base64.getEncoder().encodeToString(cert.getEncoded()));
        certInfoEntity.setPublicKey(Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded()));
        certInfoEntity.setSubject(cert.getSubjectDN().getName());
        certInfoEntity.setIssuer(cert.getIssuerDN().getName());
        certInfoEntity.setSerialNo(cert.getSerialNumber().longValue());
        certInfoEntity.setSigAlgName(cert.getSigAlgName());
        certInfoEntity.setSigAlgOid(cert.getSigAlgOID());
        certInfoEntity.setSignature(ByteUtils.toHexString(cert.getSignature()));
        certInfoEntity.setEffectiveTime(cert.getNotBefore());
        certInfoEntity.setExpireTime(cert.getNotAfter());
        certInfoEntity.setTBSCertificate(ByteUtils.toHexString(cert.getTBSCertificate()));
        return certInfoEntity;
    }

    public static CertInfoEntity getCertInfo(X509Certificate cert) throws CertificateException {
        CertInfoEntity certInfoEntity = new CertInfoEntity();
        certInfoEntity.setVersion(cert.getVersion());
        certInfoEntity.setCertInfo(Base64.getEncoder().encodeToString(cert.getEncoded()));
        certInfoEntity.setPublicKey(Base64.getEncoder().encodeToString(cert.getPublicKey().getEncoded()));
        certInfoEntity.setSubject(cert.getSubjectDN().getName());
        certInfoEntity.setIssuer(cert.getIssuerDN().getName());
        certInfoEntity.setSerialNo(cert.getSerialNumber().longValue());
        certInfoEntity.setSigAlgName(cert.getSigAlgName());
        certInfoEntity.setSigAlgOid(cert.getSigAlgOID());
        certInfoEntity.setSignature(ByteUtils.toHexString(cert.getSignature()));
        certInfoEntity.setEffectiveTime(cert.getNotBefore());
        certInfoEntity.setExpireTime(cert.getNotAfter());
        certInfoEntity.setTBSCertificate(ByteUtils.toHexString(cert.getTBSCertificate()));
        return certInfoEntity;
    }

    //生成子系统证书sql
    public static void genSubSystemCert() throws Exception {
        String smsContent = "MIIBtTCCAVygAwIBAgIIb1WOTsRGizUwCgYIKoEcz1UBg3UwPDEPMA0GA1UEBgwG5Lit5Zu9MRgwFgYDVQQDDA/lvq7kvY3moLnor4HkuaYxDzANBgNVBAoMBuW+ruS9jTAeFw0yMjA3MDQwOTQ4MTlaFw0yNTA3MDQwOTQ4MTlaMGcxDzANBgNVBAMMBkNIQUtFTjELMAkGA1UEBhMCY24xEjAQBgNVBAgMCUd1YW5nZG9uZzERMA8GA1UEBwwIU2hlbnpoZW4xDzANBgNVBAoMBkNIQUtFTjEPMA0GA1UECwwGQ0hBS0VOMFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEb28vVzr4DISmKEwW7tUI9A9JSNEhtFVnvAkiNPaW6krvHiijsnXiBW/tk61rLh6LPfEvoeTsTAYZDZtXUKHM3KMdMBswCwYDVR0PBAQDAgUgMAwGA1UdEwEB/wQCMAAwCgYIKoEcz1UBg3UDRwIwRAIgA6lzmBkH50HE6UFhCOuIEbZZVOztPGrjt9UaVw8xKX4CIB+k25gxbxqpvSb1NdDmOZJexkNRP/vSklGyyO8PBC58";
        String mssContent = "MIIBtzCCAV2gAwIBAgIJAJbAkgdbvJEhMAoGCCqBHM9VAYN1MDwxDzANBgNVBAYMBuS4reWbvTEYMBYGA1UEAwwP5b6u5L2N5qC56K+B5LmmMQ8wDQYDVQQKDAblvq7kvY0wHhcNMjIwNzA0MDk0OTU3WhcNMjUwNzA0MDk0OTU3WjBnMQ8wDQYDVQQDDAZDSEFLRU4xCzAJBgNVBAYTAmNuMRIwEAYDVQQIDAlHdWFuZ2RvbmcxETAPBgNVBAcMCFNoZW56aGVuMQ8wDQYDVQQKDAZDSEFLRU4xDzANBgNVBAsMBkNIQUtFTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABEgiYMr/OBF7ht+9tqsqCLO/Jkdn1PXWfd+1aBXOqaqZDxaVKSfdVuMOe/86M5AP2lbs1De//NEx4o9rtk4xhECjHTAbMAsGA1UdDwQEAwIFIDAMBgNVHRMBAf8EAjAAMAoGCCqBHM9VAYN1A0gDMEUCIEuCLzn2PuwFpVBfQn54CiLqkJxISiPRU22q8QwG6BGCAiEArBmtl4QXaIOYRg3GeQyLLRzsGEg7tm2amdzxIEvzbTg=";
        String crtContent = "MIIBtzCCAV2gAwIBAgIJAK1fNOKddrHKMAoGCCqBHM9VAYN1MDwxDzANBgNVBAYMBuS4reWbvTEYMBYGA1UEAwwP5b6u5L2N5qC56K+B5LmmMQ8wDQYDVQQKDAblvq7kvY0wHhcNMjIwNzA0MDk0OTA4WhcNMjUwNzA0MDk0OTA4WjBnMQ8wDQYDVQQDDAZDSEFLRU4xCzAJBgNVBAYTAmNuMRIwEAYDVQQIDAlHdWFuZ2RvbmcxETAPBgNVBAcMCFNoZW56aGVuMQ8wDQYDVQQKDAZDSEFLRU4xDzANBgNVBAsMBkNIQUtFTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABDrxTbJE+tDU+PruDptU3JmuqXZvjY+NWzLUQrakCZ7or80kaFrWxT9ynpScl3LiNp5sKJ3eEwpMH/W+NYcHk3CjHTAbMAsGA1UdDwQEAwIFIDAMBgNVHRMBAf8EAjAAMAoGCCqBHM9VAYN1A0gCMEUCIAFD/kLAZR03PCf5ZP9JzMEHdSaEJAgdemF0+0X17z7bAiEAth3sZ15Mjap3PH5oFC2OTPsp9Z5ZaUf/QEZVql2TSTw=";
        String omsContent = "MIIBtzCCAV2gAwIBAgIJAMqFeHKSPLlrMAoGCCqBHM9VAYN1MDwxDzANBgNVBAYMBuS4reWbvTEYMBYGA1UEAwwP5b6u5L2N5qC56K+B5LmmMQ8wDQYDVQQKDAblvq7kvY0wHhcNMjIwNzA0MDk1MDUyWhcNMjUwNzA0MDk1MDUyWjBnMQ8wDQYDVQQDDAZDSEFLRU4xCzAJBgNVBAYTAmNuMRIwEAYDVQQIDAlHdWFuZ2RvbmcxETAPBgNVBAcMCFNoZW56aGVuMQ8wDQYDVQQKDAZDSEFLRU4xDzANBgNVBAsMBkNIQUtFTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABCi8I7QMiIouII/2yc309xFOhT/pZ12vZDSc9/fks6dVHLqJtGQ62g1kkmxP9PTxBHNtZKwO5mVAJfHoXiDVo6mjHTAbMAsGA1UdDwQEAwIFIDAMBgNVHRMBAf8EAjAAMAoGCCqBHM9VAYN1A0gBMEUCIQDcdwYvs2KI6ENSjW6I8PZQ1OfKeQtUHFpLU5+CrL8L1AIgBByaHdyhvqz+8FkPV6K/fOFfPbKW0I3+4ykkLaMpFRo=";


        Map<String, String> subCert = new HashMap<>();
        subCert.put("1", smsContent);
        subCert.put("2", crtContent);
        subCert.put("3", mssContent);
        subCert.put("4", omsContent);

        String model = "update `sub_system_cert` set \n" +
                "`public_ticket` = '%s', \n" +
                "`sign_ticket` = null, \n" +
                "`cert_ticket` = '%s', \n" +
                "`cert_create_time` = '%s', \n" +
                "`cert_expire_time` = '%s', \n" +
                "`issuer` = '%s', \n" +
                "`subject` = '%s', \n" +
                "`cert_version` = '%s', \n" +
                "`serial_number` = '%s', \n" +
                "`update_time` = now() \n" +
                "where `sub_system_id` = '%s' and `version_ext` = '%s';";
        for (Map.Entry<String, String> subInfo : subCert.entrySet()) {

            String subSystemId = subInfo.getKey();    // todo 改成分系统的id，看sub_system_info表
            String versionext = "02"; // todo 改成授权中心对应的versionext
            //String certFilePath = "C:\\Users\\wds\\Desktop\\201可信授权系统签发的证书\\服务支撑系统-服务支撑分系统-微位.pem"; // todo 改成分系统的票据的文件地址
            //String content = FileUtils.readFileToString(new File(certFilePath), StandardCharsets.UTF_8);
            String content = subInfo.getValue();
            if (!content.startsWith("-----BEGIN")) {
                content = "-----BEGIN CERTIFICATE-----\n" + content + "\n-----END CERTIFICATE-----";
            }
            CertInfoEntity certInfo = PemUtils.getCertInfo(content);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sql = String.format(model, certInfo.getPublicKey(), certInfo.getCertInfo(),
                    simpleDateFormat.format(certInfo.getEffectiveTime()), simpleDateFormat.format(certInfo.getExpireTime()),
                    certInfo.getIssuer(), certInfo.getSubject(),
                    certInfo.getVersion(), certInfo.getSerialNo(),
                    subSystemId, versionext);
            System.out.println(sql);
        }


    }


    public static void main(String[] args) throws Exception {
//          test();

//        PemEntity entity = parsePem("C:\\Users\\wds\\Desktop\\201可信授权系统签发的证书\\服务支撑系统-服务支撑分系统-微位.pem");
//        CertInfoEntity entity1 = getCertInfo(entity);
//        System.out.println(entity1);

        // 2-签名
//        CertInfoEntity info1 = PemUtils.getCertInfo("-----BEGIN CERTIFICATE-----\n"
//                + "MIIB2DCCAX6gAwIBAgIBCzAKBggqgRzPVQGDdTBHMQswCQYDVQQGDAJDTjEOMAwGA1UECgwFV2V3YXkxKDAmBgNVBAMMH1RydXN0ZWQgYXV0aG9yaXphdGlvbiBzdWJzeXN0ZW0wIhgPMjAyMjA1MjQyMzUyNDhaGA8yMDUyMDUyNDIzNTI0OFowezENMAsGA1UEAwwEc3pjYTELMAkGA1UEBhMCY24xEjAQBgNVBAgMCUd1YW5nZG9uZzERMA8GA1UEBwwIU2hlbnpoZW4xJzAlBgNVBAoMHlNoZW5aaGVuIENlcnRpZmljYXRlIEF1dGhvcml0eTENMAsGA1UECwwEc3pjYTBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABM0VGPymbjOR3w7XEyYxRR1YDcwizVliWBLt2dxIZROsWi61dZfVArQzfEcME0CICUVLkE6BUbLFvKSS7YYM7I6jIzAhMA4GA1UdDwEBAAQEAwICBDAPBgNVHRMBAQAEBTADAQEAMAoGCCqBHM9VAYN1A0gAMEUCIH6/01QW/5CqhO9pNlZael1DVhlfSyU9HMQ292YsiIo6AiEA9susoww0mldnCRtesqMzGKP0R9aZmHWswvpGW9JOkng=" +
//                "\n-----END CERTIFICATE-----");
//
//        final String cert = "nxEHgc1OEvsrrVhAuAVGqV2TfsUHtgl1VD++6mtzmLDk+h0H8AZ820QphCxDFiNwlTdwXHuCMn7z+jKdNDSpiNBPRY+OF+zn99e4RWA/sTivzkoY3L9tbEeVjKOGeJDuTxOxzh4pwdD9ysK56YMHHsJlY4LwzCNIYa+JpedaKqiKyV99k3S4Xpge3K3EJNmAMYMgGapv/tSKO3gCoTm0or37va0zLLNL/683OYKMvfykaa1um/sFmzfbFrnhRcctIla/apO/jpKfy401+bAdYxRLPZK6iIU00n/45qgkxO+QxdRvpiU7ciZNWD/rHNsCYSkRbMIUkehkX/IqRfprZSb5UGdytAj/UpjNFj+PsB/daM08h/970qYOdQ1BGyrdtQLU2X+E2T8779qTVjaw1Gjrx6JPlnzoUHwIRNs4rH7d2tV6zFVBwth1S7NqeO5AIQNRQEcUp3J7+5SmVco0LjAIE9Xf7Su1w/m/xx4F6iBGLOuwg+tfqm6Cgx1yXcMDvPRlXSXANETJDb+UItIRPL4u2ao+XItUC6ZltTQfq4ZCKrNlwfl9MajkDE2ROkslvvESKJwTw7GMkfNm8a0wV6I3K7N7uTfdPfHfpdJgtee3xLElPgLFotoR33Y8ICtlww5rcnHZ9XcDg5jPTgEiy3cs7mDY+SNfPleayvaPiNvrdsRjK3k8v0dLTj9RtCGuKGKUVr0sj4SSgUnZnlPOFE6LeCF2o1Ekh+fNI6gMGM2GU0RcDeMDUZbM10x8ep3QBalPjrLGnHNOShUwSXO3QncKx8BpMph+bwdjIz4+B7WdckQ6+5ZW764Bw8KrvqDJ4mQyGO8A8uF0Cr9nLj4VEGcG+VH1W8ZIG9X5S2yj7aKVv/S3qtyrWymgB0Mora8JuHL3zzCdC2MH5Dd5JREu5b8IVUZm0M8RR9ArE/nNgBALxI4dRVmEwhrFJzoi2czi5F491Ov8UXqsgLdKZ/y5b2jnaKRufKEiYj5yFpnXVEv0wRKsxu6TnyoRjAPf/R5bIbN/TITtpirmBEJOTU5SSpMZ5hdqCT9uT6uLN8cUBO5BeJEB3i3gogXeMJZXayanAcORwoI8aNGl2acPRLHIEjD+cfLSPFCY03aXVYnEkGnaYtvYS4RiTGso1fDIns/5KzIh940kKJtFn7JtELoGGFE85Yy0zFYuAwqq6xYOYf2b/QhOqXL39o1Gv7fOhuM9FRmPlN0vPd/47EPyqEFXwkNcHsp9xLqY6/7fO5abtMaws70BuK+/vcXuZ6076L2gc6lBnQqEH2X6ALa+tjhNeHX7e5mXm7mtc73f44fthVGnmf6RjK0K9/QYrfVfXwIyhx/1p5vrabOkf+2yxmerJRw0Va6qOy0GRiOWanVCe6sgw4Ar+/05Fw8mABEr6btr8lASD+ChFDIme3ilXfDagybkZ4s+WdAj+ApQkvcx2fCkYqhg56amusa3P/MaHqqlV5SaaeaBO4na83xcUGGWs+PuLieNgmnKHOnUWt/U6ZiTnZHNrwhTNWjg/GGfDC6CNcTqM7eLcZv+Q7i3xYLleD79BbDkxmq4Yqt8mbdii7QqmPYoXlJKK01aAKIEQOlgIkQPLTip6gU8kAJDDZ3LHwbCbGVDFo8NFfQZAlKeB5vtv/fXNQDzmAsdkLYdcrMxFtQedMut4JRVxfchn1zgwvFc+SkKEbNMGigrRwh/mGY=";
//        // 2-证书
//        CertInfoEntity info12 = PemUtils.getCertInfo("-----BEGIN CERTIFICATE-----\n" + cert +
//                "-----END CERTIFICATE-----");
//        System.out.println(JacksonUtils.toJson(info1.getPublicKey()));
        CertInfoEntity info2 = PemUtils.getCertInfo("-----BEGIN CERTIFICATE-----\n"
                + "MIIBwDCCAW2gAwIBAgIBGjAKBggqgRzPVQGDdTBHMQswCQYDVQQGEwJDTjEVMBMG\n" +
                "A1UECgwM5b6u5L2N56eR5oqAMSEwHwYDVQQDDBjlj6/kv6HmnI3liqHmjojmnYPk\n" +
                "uK3lv4MwIhgPMjAyMjAxMjYwNzMzNTJaGA8yMDI1MDEyNTA3MzM1MlowezELMAkG\n" +
                "A1UEBhMCY24xEjAQBgNVBAgTCUd1YW5nZG9uZzERMA8GA1UEBxMIU2hlbnpoZW4x\n" +
                "JzAlBgNVBAoTHlNoZW5aaGVuIENlcnRpZmljYXRlIEF1dGhvcml0eTENMAsGA1UE\n" +
                "CxMEc3pjYTENMAsGA1UEAxMEc3pjYTBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IA\n" +
                "BMb5VhmyH2klJrxK73JxUomCQJJk3XWtHsuNx3fu2hNHDBrWfn8anXaWIuZ0fKwW\n" +
                "cgpsQJsaSirMZbO1TuhBWrSjEjAQMA4GA1UdDwEB/wQEAwIEEDAKBggqgRzPVQGD\n" +
                "dQNBAOCiJ0Cdi8fq57Ys2hCjOPqGj1IMOy8v4wl7psHCsVtJj2uFPhXuCRf7br3f\n" +
                "/RlJrUNzUoXyd8yYRNV6/LdkR/M=" +
                "\n-----END CERTIFICATE-----");
        System.out.println("" + JacksonUtils.toJson(info2.getPublicKey()));
//        CertInfoEntity info3 = PemUtils.getCertInfo("-----BEGIN CERTIFICATE-----\n"
//                + "MIIBqjCCAVCgAwIBAgIQSoGofrD3QvhS1rIGdpM1pjAKBggqgRzPVQGDdTBHMQsw\n" +  2
//                "CQYDVQQGDAJDTjEOMAwGA1UECgwFV2V3YXkxKDAmBgNVBAMMH1RydXN0ZWQgYXV0\n" +
//                "aG9yaXphdGlvbiBzdWJzeXN0ZW0wHhcNMjIwNjIwMDk0OTUyWhcNMjUwNjIwMDk0\n" +
//                "OTUyWjBFMQ0wCwYDVQQDDARzemNhMScwJQYDVQQKDB5TaGVuWmhlbiBDZXJ0aWZp\n" +
//                "Y2F0ZSBBdXRob3JpdHkxCzAJBgNVBAYMAmNuMFkwEwYHKoZIzj0CAQYIKoEcz1UB\n" +
//                "gi0DQgAEzRUY/KZuM5HfDtcTJjFFHVgNzCLNWWJYEu3Z3EhlE6xaLrV1l9UCtDN8\n" +
//                "RwwTQIgJRUuQToFRssW8pJLthgzsjqMgMB4wDgYDVR0PAQH/BAQDAgbAMAwGA1Ud\n" +
//                "EwEB/wQCMAAwCgYIKoEcz1UBg3UDSAAwRQIgCI8Mk9SIguG7GYZsQ9ICddYYD4ut\n" +
//                "axU9W6p0Gf01UQICIQCehdM3ZFM9G2qSl39rPScQnHxBQFpX2nXdyQ/ghPGs5A==" +
//                "\n-----END CERTIFICATE-----");
//        System.out.println(""+JacksonUtils.toJson(info3.getPublicKey()));

        // 2-加密
//        CertInfoEntity info2 = PemUtils.getCertInfo("-----BEGIN CERTIFICATE-----\n" +
//                "MIIBujCCAWGgAwIBAgIJAJHNU+IujbxHMAoGCCqBHM9VAYN1MDwxDzANBgNVBAYMBuS4reWbvTEYMBYGA1UEAwwP5b6u5L2N5qC56K+B5LmmMQ8wDQYDVQQLDAblvq7kvY0wIhgPMjAyMjA2MTQxNjAwMDBaGA8yMDI1MDYxNDE2MDAwMFowZzEPMA0GA1UEAwwGQ0hBS0VOMQswCQYDVQQGEwJjbjESMBAGA1UECAwJR3Vhbmdkb25nMREwDwYDVQQHDAhTaGVuemhlbjEPMA0GA1UECgwGQ0hBS0VOMQ8wDQYDVQQLDAZDSEFLRU4wWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATrK9P7nx+aa78d8yFwrxdZO5XvkcBXORjxHI8uSs6To0e1lENGf7rubbixys7jTPYev5NDnSQNIDRXwPb2mk80ox0wGzALBgNVHQ8EBAMCAAMwDAYDVR0TBAUwAwEBADAKBggqgRzPVQGDdQNHADBEAiBWw5tE7Qd0YUY3p2cqedAXjGm+INqSc5JtNWxeSBInnwIgRMHKCHLYJeo3VLWYRGEkiXu4tfI9j8AJXo1TeF5BPTM=" +
//                "-----END CERTIFICATE-----");
//        System.out.println(JacksonUtils.toJson(info2.getPublicKey()));
    }
}
