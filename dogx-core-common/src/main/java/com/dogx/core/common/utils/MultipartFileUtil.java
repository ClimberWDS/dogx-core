package com.dogx.core.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 描述：
 *
 * @author yangp.
 * @date 2021/9/6.
 */
@Slf4j
@Component
public class MultipartFileUtil {

    private final static Integer FILE_SIZE_3 = 3;//文件上传限制大小
    private final static Integer FILE_SIZE_5 = 5;//文件上传限制大小
    private final static Integer FILE_SIZE_10 = 10;//文件上传限制大小
    private final static Integer FILE_SIZE_20 = 20;//文件上传限制大小
    private final static String FILE_UNIT = "M";//文件上传限制单位（B,K,M,G）

    /**
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @描述 判断文件大小
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equalsIgnoreCase(unit)) {
            fileSize = (double) len;
        } else if ("K".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1024;
        } else if ("M".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1048576;
        } else if ("G".equalsIgnoreCase(unit)) {
            fileSize = (double) len / 1073741824;
        }
        log.info("压缩后的文件大小：{}",fileSize);
        return !(fileSize > size);
    }

    //文件上传调用
    public static boolean upload(MultipartFile file) {
        boolean flag = checkFileSize(file.getSize(), FILE_SIZE_3, FILE_UNIT);
        if (!flag) {
            throw new RuntimeException("单个上传文件大小不能超过(" + FILE_SIZE_3 + FILE_UNIT + ")限制");
        }
        return flag;
    }

    //文件上传调用
    public static boolean uploadLimit(MultipartFile file) {
        boolean flag = checkFileSize(file.getSize(), FILE_SIZE_10, FILE_UNIT);
        if (!flag) {
            throw new RuntimeException("单个文件上传大小不能超过(" + FILE_SIZE_10 + FILE_UNIT + ")限制");
        }
        return flag;
    }

    /**
     * 多文件上传大小限制
     * @return
     */
    public static boolean multiUploadLimit(List<MultipartFile> fileList){
        long fileSize = 0;
        boolean flag = true;
        if (null != fileList && fileList.size() > 0) {
            fileSize = fileList.stream().mapToLong(MultipartFile::getSize).sum();
        }
        flag = checkFileSize(fileSize, FILE_SIZE_10, FILE_UNIT);
        return flag;
    }
}
