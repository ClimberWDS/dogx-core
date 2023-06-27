package com.dogx.core.common.utils;

import com.dogx.core.base.model.BusinessException;
import com.dogx.core.base.model.ExtraCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.net.ssl.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author 65796.
 * @date 2021/5/7.
 */
@Slf4j
public class FileUtil {

    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    public MultipartFile fileToMultipartFile(File file) throws IOException {
        FileItem fileItem = createFileItem(file);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    public static boolean checkFileHash(String url) throws IOException {
        String[] split = url.split("/");
        String hash = split[split.length - 1];
        hash = hash.substring(0, hash.lastIndexOf("."));
        MultipartFile multipartFile = FileUtil.urlImageToMultipartFile(url);
        if (multipartFile == null) {
            log.warn("multipartFile为空");
            return false;
        }
        String fileHash = ByteUtils.toHexString(SoftSM3Util.hash(multipartFile.getBytes()));
        if (!fileHash.equals(hash)) {
            return false;
        }
        return true;
    }

    public static boolean checkBigFileHash(String url) throws IOException {
        String[] split = url.split("/");
        String hash = split[split.length - 1];
        hash = hash.substring(0, hash.lastIndexOf("."));
        byte[] bytes = SoftSM3Util.encrypt(getInputStream(url));
        if (ArrayUtils.isEmpty(bytes)) {
            return false;
        }
        String fileHash = ByteUtils.toHexString(bytes);
        if (!fileHash.equals(hash)) {
            return false;
        }
        return true;
    }


    private static FileItem createFileItem(File file) throws IOException {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = item.getOutputStream()) {
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("[FileUtil 异常] createFileItem异常", e);
        }
        return item;
    }

    public static void saveFile(InputStream inputStream, String fileName) {
        OutputStream os = null;
        try {
            String path = "/tmp/sm4/";
            // 1K的数据缓冲
            byte[] bs = new byte[8192];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        } catch (IOException e) {
            log.error("IOException:", e);
        } catch (Exception e) {
            log.error("Exception:", e);
        } finally {
            // 完毕，关闭所有链接
            try {
                if (os != null) {
                    os.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("IOException:", e);
            }
        }
    }

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 获取文件名，排除后缀
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public static String getFileNameWithSuffix(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    /**
     * url图片转MultipartFile
     *
     * @param
     * @return
     */
    public static MultipartFile urlImageToMultipartFile(String url) throws IOException {
        MultipartFile multipartFile = null;
        InputStream inputStream = null;
        if (StringUtils.isNotEmpty(url)) {
            String[] split = url.split("/");
            try {
                byte[] bytes = getFile(url);
                if (bytes == null) {
                    log.error("从网络下载图片地址失败url：" + url + ",bytes:null");
                    throw new BusinessException(ExtraCodeEnum.UPLOAD_FILE_FAIL);
                }
                if (bytes.length == 0) {
                    log.warn("从网络下载图片地址失败url：" + url + ",bytes-length:" + bytes.length);
                }
                inputStream = new ByteArrayInputStream(bytes, 0, bytes.length);
                multipartFile = getMultipartFile(inputStream, split[split.length - 1]);
            } catch (Exception e) {
                log.error("从网络下载图片地址失败url ：" + url, e);
                throw new BusinessException(ExtraCodeEnum.UPLOAD_FILE_FAIL);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }
        return multipartFile;
    }


    public static String getFileHash(String mediaUrl) {
        try {
            if (StringUtils.isBlank(mediaUrl)) {
                return "";
            }
            MultipartFile multipartFile = urlImageToMultipartFile(mediaUrl);
            if (multipartFile == null) {
                log.warn("multipartFile为空");
                return "";
            }
            byte[] hash = SoftSM3Util.hash(multipartFile.getBytes());
            return ByteUtils.toHexString(hash);
        } catch (Exception e) {
            log.error("下载网络图片失败url:" + mediaUrl, e);
            String fileHash = mediaUrl.substring(mediaUrl.lastIndexOf("/") + 1, mediaUrl.lastIndexOf("."));
            return fileHash;
        }
    }


    /**
     * 得到文件流
     *
     * @param url 图片地址
     * @return
     */
    public static byte[] getFileStream(String url) {
        try {
            url = url.replaceAll(" ", "%20");
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static byte[] getFile(String strUrl) throws Exception {
        try {
            //  直接通过主机认证
            HostnameVerifier hv = new HostnameVerifier() {
                @Override
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            //  配置认证管理器
            TrustManager[] trustAllCerts = {new TrustAllTrustManager()};
            SSLContext sc = SSLContext.getInstance("SSL");
            SSLSessionContext sslContent = sc.getServerSessionContext();
            sslContent.setSessionTimeout(0);
            sc.init(null, trustAllCerts, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            //  激活主机认证
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inStream = connection.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            String myHash = ByteUtils.toHexString(SoftSM3Util.hash(btImg));
            System.out.println(myHash);
            return btImg;
        } catch (Exception e) {
            log.error("下载非安全证书请求失败", e);
            throw e;
        }
    }

    public static class TrustAllTrustManager implements TrustManager, X509TrustManager {

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

    }

    public static InputStream getInputStream(String url) {
        try {
            url = url.replaceAll(" ", "%20");
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            return inStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 获取封装得MultipartFile
     *
     * @param inputStream inputStream
     * @param fileName    fileName
     * @return MultipartFile
     */
    public static MultipartFile getMultipartFile(InputStream inputStream, String fileName) {
        FileItem fileItem = createFileItem(inputStream, fileName);
        //CommonsMultipartFile是feign对multipartFile的封装，但是要FileItem类对象
        return new CommonsMultipartFile(fileItem);
    }


    /**
     * FileItem类对象创建
     *
     * @param inputStream inputStream
     * @param fileName    fileName
     * @return FileItem
     */
    public static FileItem createFileItem(InputStream inputStream, String fileName) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "file";
        FileItem item = factory.createItem(textFieldName, MediaType.MULTIPART_FORM_DATA_VALUE, true, fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        OutputStream os = null;
        //使用输出流输出输入流的字节
        try {
            os = item.getOutputStream();
            while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (IOException e) {
            log.error("Stream copy exception", e);
            throw new IllegalArgumentException("文件上传失败");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log.error("Stream close exception", e);

                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Stream close exception", e);
                }
            }
        }

        return item;
    }


    /**
     * multipartFile转BufferedImage
     *
     * @param file
     */
    public static BufferedImage multipartFileToBufferedImage(MultipartFile file) throws IOException {
        BufferedImage img = null;
        InputStream inputStream = null;
        try {
            byte[] byteArr = file.getBytes();
            inputStream = new ByteArrayInputStream(byteArr);
            img = ImageIO.read(inputStream);
        } catch (Exception e) {
            log.error("multipartFileToBufferedImage异常", e);
            throw new BusinessException(ExtraCodeEnum.UPLOAD_FILE_FAIL);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return img;
    }

    /**
     * 校验文件限制大小
     *
     * @param authorizationUrl
     * @param companyLicenseUrl
     * @param idCardPositiveUrl
     * @param idCardReverseUrl
     * @return
     * @throws IOException
     */
    public static boolean checkMultiFileLimit(String authorizationUrl, String companyLicenseUrl, String idCardPositiveUrl, String idCardReverseUrl) throws IOException {
        List<MultipartFile> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(authorizationUrl)) {
            MultipartFile multipartFile = urlImageToMultipartFile(authorizationUrl);
            list.add(multipartFile);
        }
        if (StringUtils.isNotEmpty(companyLicenseUrl)) {
            MultipartFile companyLicenseUrlFile = urlImageToMultipartFile(companyLicenseUrl);
            list.add(companyLicenseUrlFile);
        }
        if (StringUtils.isNotEmpty(idCardPositiveUrl)) {
            MultipartFile idCardPositiveUrlFile = urlImageToMultipartFile(idCardPositiveUrl);
            list.add(idCardPositiveUrlFile);
        }
        if (StringUtils.isNotEmpty(idCardReverseUrl)) {
            MultipartFile idCardReverseUrlFile = urlImageToMultipartFile(idCardReverseUrl);
            list.add(idCardReverseUrlFile);
        }
        return MultipartFileUtil.multiUploadLimit(list);
    }


}
