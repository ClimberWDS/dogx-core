package com.dogx.core.base.utils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Objects;

/**
 * 描述：
 *
 * @author yangp.
 * @date 2021/11/5.
 */
public class PicUtils {
    //以下是常量,按照阿里代码开发规范,不允许代码中出现魔法值
    private static final Logger logger = LoggerFactory.getLogger(PicUtils.class);
    private static final Integer ZERO = 0;
    public static final Integer ONE_ZERO_TWO_FOUR = 1024;
    public static final float LIMIT_MB = 3;

    private static final Integer NINE_ZERO_ZERO = 900;
    private static final Integer THREE_TWO_SEVEN_FIVE = 3275;
    private static final Integer TWO_ZERO_FOUR_SEVEN = 2047;
    private static final Double ZERO_EIGHT_FIVE = 0.85;
    private static final Double ZERO_SIX = 0.6;
    private static final Double ZERO_FOUR_FOUR = 0.44;
    private static final Double ZERO_FOUR = 0.4;
    public static final long DES_FILE_SIZE_700 = 700;

    /**
     * 压缩因子
     */
    public static final float FACTOR = 0.4f;

    /**
     * 压缩次数统计
     */
    private static final ThreadLocal<Integer> counter = new ThreadLocal<>();

    /**
     * pdf压缩次数
     * 10M 128M内存 超过2次将会内存泄露
     * 10M 256M 超过5次将会内存泄露
     */
    private static final Integer PDF_COMPRESS_LIMIT = 2;

    private static void addCount() {
        Integer count = counter.get();
        if (null == count) {
            counter.set(1);
        } else {
            counter.set(++count);
        }
    }

    private static Integer getCount() {
        return counter.get();
    }

    private static void clearCount() {
        counter.remove();
    }

    /**
     * 根据指定大小压缩图片
     *
     * @param imageBytes  源图片字节数组
     * @param desFileSize 指定图片大小，单位kb
     * @return 压缩质量后的图片字节数组
     */
    public static byte[] compressPicForScale(byte[] imageBytes, long desFileSize) throws IOException {
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        if (imageBytes == null || imageBytes.length <= ZERO || imageBytes.length < desFileSize * ONE_ZERO_TWO_FOUR) {
            return imageBytes;
        }
        long srcSize = imageBytes.length;
        double accuracy = getAccuracy(srcSize / ONE_ZERO_TWO_FOUR);
        try {
            while (imageBytes.length > desFileSize * ONE_ZERO_TWO_FOUR) {
                inputStream = new ByteArrayInputStream(imageBytes);
                outputStream = new ByteArrayOutputStream(imageBytes.length);
                Thumbnails.of(inputStream)
                        .scale(accuracy)
                        .outputQuality(accuracy)
                        .toOutputStream(outputStream);
                imageBytes = outputStream.toByteArray();
            }
        } catch (Exception e) {
            logger.error("【图片压缩】msg=图片压缩失败!", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
        return imageBytes;
    }

    /**
     * 预估值，压缩因子。根据实际测试结果返回
     *
     * @param length 字节
     * @param limit  字节
     * @return
     */
    private static float getFactor(float length, float limit) {
        float mb = (float) ONE_ZERO_TWO_FOUR * ONE_ZERO_TWO_FOUR;
        if (length < limit * mb) {
            return 1;
        } else if (length < 3.5 * mb) {
            return 0.9f;
        } else if (length < 4 * mb) {
            return 0.8f;
        } else if (length < 5 * mb) {
            return 0.75f;
        } else if (length < 6 * mb) {
            return 0.7f;
        } else if (length < 7 * mb) {
            return 0.64f;
        } else if (length < 8 * mb) {
            return 0.5f;
        } else if (length < 9 * mb) {
            return 0.45f;
        } else if (length < 10 * mb) {
            return 0.4f;
        }
        return 1;
    }

    public static File manipulatePdf(byte[] fileByte, float limit) {
        return manipulatePdf(fileByte, null, limit);
    }

    public static File manipulatePdf(String src, float limit) {
        return manipulatePdf(null, src, limit);
    }

    /**
     * @param src   源文件
     * @param limit 文件大小限制 ，单位M
     * @throws DocumentException
     * @throws IOException
     */
    public static File manipulatePdf(byte[] fileByte, String src, float limit) {
        try {
            if (fileByte == null && StringUtils.isAnyBlank(src)) {
                return null;
            }
            addCount();
            PdfReader reader = fileByte == null ? new PdfReader(src) : new PdfReader(fileByte);
            int n = reader.getXrefSize();
            float fileLength = reader.getFileLength();
            //少于指定大少不做处理
            if (fileLength < limit * ONE_ZERO_TWO_FOUR * ONE_ZERO_TWO_FOUR) {
                return null;
            }
            float factor = getFactor(fileLength, PicUtils.LIMIT_MB);
            PdfObject object;
            PRStream stream;
            // Look for image and manipulate image stream
            for (int i = 0; i < n; i++) {
                object = reader.getPdfObject(i);
                if (object == null || !object.isStream()) {
                    continue;
                }
                stream = (PRStream) object;
                if (!PdfName.IMAGE.equals(stream.getAsName(PdfName.SUBTYPE))) {
                    continue;
                }
                if (!PdfName.DCTDECODE.equals(stream.getAsName(PdfName.FILTER))) {
                    continue;
                }
                PdfImageObject image = new PdfImageObject(stream);
                BufferedImage bi = image.getBufferedImage();
                if (bi == null) {
                    continue;
                }
                int width = (int) (bi.getWidth() * factor);
                int height = (int) (bi.getHeight() * factor);
                if (width <= 0 || height <= 0) {
                    continue;
                }
                BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                AffineTransform at = AffineTransform.getScaleInstance(factor, factor);
                Graphics2D g = img.createGraphics();
                g.drawRenderedImage(bi, at);
                ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
                ImageIO.write(img, "JPG", imgBytes);
                stream.clear();
                stream.setData(imgBytes.toByteArray(), false, PRStream.NO_COMPRESSION);
                stream.put(PdfName.TYPE, PdfName.XOBJECT);
                stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
                stream.put(PdfName.FILTER, PdfName.DCTDECODE);
                stream.put(PdfName.WIDTH, new PdfNumber(width));
                stream.put(PdfName.HEIGHT, new PdfNumber(height));
                stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
            }
            reader.removeUnusedObjects();
            File tempStorage = File.createTempFile("tempPdf", ".pdf");
            tempStorage.deleteOnExit();
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(tempStorage.getAbsolutePath()));
            stamper.setFullCompression();
            stamper.close();
            reader.close();
            long length = tempStorage.length();
            if (length > limit * ONE_ZERO_TWO_FOUR * ONE_ZERO_TWO_FOUR && getCount() <= PDF_COMPRESS_LIMIT) {
                File file = manipulatePdf(tempStorage.getAbsolutePath(), PicUtils.LIMIT_MB);
                logger.info("临时文件生成目录，{}", tempStorage.getAbsolutePath());
                tempStorage.delete();
                stamper.close();
                reader.close();
                return file;
            }
            clearCount();
            return tempStorage;
        } catch (Exception e) {
            logger.error("压缩pdf错误", e);
            return null;
        }

    }

    public static File compressPDFWithItext(byte[] fileByte, String src) {
        if (fileByte == null && StringUtils.isBlank(src)) {
            return null;
        }
        try {
            PdfReader reader = fileByte == null ? new PdfReader(src) : new PdfReader(fileByte);
            File tempPdf = File.createTempFile("tempPdf", ".pdf");
            FileOutputStream outputStream = new FileOutputStream(tempPdf);
            PdfStamper stamper = new PdfStamper(reader, outputStream, PdfWriter.VERSION_1_5);
            stamper.setFullCompression();
            stamper.getWriter().setCompressionLevel(9);
            stamper.close();
            outputStream.close();
            reader.close();
            return tempPdf;
        } catch (Exception e) {
            logger.error("itext压缩pdf失败", e);
        }
        return null;
    }

    public static File compressPDFWithPDFBox(byte[] fileByte, String src) {
        if (fileByte == null && StringUtils.isBlank(src)) {
            return null;
        }
        try {
            File tempPdf = File.createTempFile("tempPdf", ".pdf");
            PDDocument doc = fileByte == null ? PDDocument.load(new File(src)) : PDDocument.load(fileByte);
            PDStream stream = new PDStream(doc);
            stream.createOutputStream(COSName.FLATE_DECODE);
            doc.save(tempPdf.getAbsolutePath());
            doc.close();
            return tempPdf;
        } catch (Exception e) {
            logger.error("pdfBox压缩pdf错误", e);
        }
        return null;
    }

    public static byte[] compressPDFCombination(byte[] fileByte, float limit) {
        File pdf = manipulatePdf(fileByte, limit);
        if (Objects.nonNull(pdf) && pdf.length() <= fileByte.length) {
            return pdfBoxAndItextPdf(file2byte(pdf));
        }
        return pdfBoxAndItextPdf(fileByte);
    }

    public static byte[] pdfBoxAndItextPdf(byte[] fileByte) {
        if (fileByte == null) {
            return null;
        }
        File pdfBox = compressPDFWithPDFBox(fileByte, null);
        if (Objects.nonNull(pdfBox) && pdfBox.length() <= fileByte.length) {
            byte[] pdfBoxByte = file2byte(pdfBox);
            File itextPdfInner = compressPDFWithItext(pdfBoxByte, null);
            if (Objects.nonNull(itextPdfInner) && itextPdfInner.length() <= pdfBox.length()) {
                return file2byte(itextPdfInner);
            }
            return pdfBoxByte;
        }

        File itextPdf = compressPDFWithItext(fileByte, null);
        if (Objects.nonNull(itextPdf) && itextPdf.length() <= fileByte.length) {
            byte[] itextPdfByte = file2byte(itextPdf);
            File pdfBoxInner = compressPDFWithPDFBox(itextPdfByte, null);
            if (Objects.nonNull(pdfBoxInner) && pdfBoxInner.length() <= itextPdf.length()) {
                return file2byte(pdfBoxInner);
            }
            return itextPdfByte;
        }
        return fileByte;
    }

    public static byte[] manipulatePdfReturnByteArr(byte[] fileByte, float limit) {
        File file = manipulatePdf(fileByte, limit);
        if (Objects.isNull(file)) {
            return fileByte;
        }
        return file2byte(file);
    }

    public static byte[] file2byte(File tradeFile) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            fis = new FileInputStream(tradeFile);
            bos = new ByteArrayOutputStream();
            byte[] b = new byte[8192];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            logger.error("文件未找到", e);
        } catch (IOException e) {
            logger.error("IO异常", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
        return buffer;
    }


    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < NINE_ZERO_ZERO) {
            accuracy = ZERO_EIGHT_FIVE;
        } else if (size < TWO_ZERO_FOUR_SEVEN) {
            accuracy = ZERO_SIX;
        } else if (size < THREE_TWO_SEVEN_FIVE) {
            accuracy = ZERO_FOUR_FOUR;
        } else {
            accuracy = ZERO_FOUR;
        }
        return accuracy;
    }

    public static final String TYPE_JPG = "jpg";
    public static final String TYPE_GIF = "gif";
    public static final String TYPE_PNG = "png";
    public static final String TYPE_BMP = "bmp";
    public static final String TYPE_UNKNOWN = "unknown";

    /**
     * byte数组转换成16进制字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * 根据文件流判断图片类型
     *
     * @param fis
     * @return jpg/png/gif/bmp
     */
    public static String getPicType(InputStream fis) {
        //读取文件的前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String hexString = bytesToHexString(b);
            if (hexString == null) {
                return null;
            }
            String type = hexString.toUpperCase();
            if (type.contains("FFD8FF")) {
                return TYPE_JPG;
            } else if (type.contains("89504E47")) {
                return TYPE_PNG;
            } else if (type.contains("47494638")) {
                return TYPE_GIF;
            } else if (type.contains("424D")) {
                return TYPE_BMP;
            } else {
                return null;
            }
        } catch (IOException e) {
            logger.error("IO异常", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
        return null;
    }
}
