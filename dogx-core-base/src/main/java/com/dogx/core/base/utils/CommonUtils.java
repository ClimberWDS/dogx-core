package com.dogx.core.base.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    /***
     * 判断字符串是否为空
     * @param str    输入字符串
     * @return
     */
    public static boolean isEmpty(Object str) {
        return ((str == null) || (str.toString().length() == 0) || ("null".equalsIgnoreCase(str.toString().trim())) || (str.toString().trim().length() == 0));
    }

    /***
     * 判断输入字符串符合某种格式
     * @param str    输入字符串
     * @param regex    格式表达式
     * @return
     */
    public static boolean isRegex(Object str, String regex) {
        if (str == null) {
            return false;
        }
        if (isEmpty(str)) {
            str = "";
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str.toString());
        boolean flag = matcher.matches();
        return flag;
    }

    /***
     * 判断输入字符串符合某种格式，并且长度小于len
     * @param str    输入字符串
     * @param regex    格式表达式
     * @param len    长度
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean isRegex(Object str, String regex, int len) {
        return isRegex(str, regex) && (len(str) <= len);
    }

    /***
     * 判断输入字符串符合某种格式，并且长度等于len
     * @param str    输入字符串
     * @param regex    格式表达式
     * @param len    长度
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean isFixRegex(Object str, String regex, int len) {
        return isRegex(str, regex) && (len(str) == len);
    }

    /***
     * 判断字符串不为空，且长度小于len
     * @param str    输入字符串
     * @param len    长度
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean isNotEmpty(Object str, int len) {
        return !isEmpty(str) && (len(str) <= len);
    }

    /***
     * 判断字符是否超过最大长度
     * @param str    输入字符串
     * @param len    最大长度
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean maxLen(Object str, int len) {
        return str != null && str.toString().length() <= len;
    }

    /***
     * 获取字符长度
     * @param str    输入字符串
     * @return
     * @throws UnsupportedEncodingException
     */
    public static int len(Object str) {
        if (isEmpty(str)) {
            return 0;
        }
        return str.toString().length();
    }

    /***
     * 判断是否是数字
     * @param str    输入字符串
     * @return
     */
    public static boolean isNumeric(Object str) {
        //return isRegex(str,"^\\d+$");//负数判断不了如-1
        return isRegex(str, "-?[0-9]+(.[0-9]+)?");
    }

    /***
     * 判断是否是数字
     * @param str    输入字符串
     * @param fix    固定长度
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean isFixNumeric(Object str, int fix) {
        return isRegex(str, "^\\d+$") && (len(str) == fix);
    }

    /***
     * 判断是否是数字
     * @param str    输入字符串
     * @param max    最大长度
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean isNumeric(Object str, int max) {
        return isRegex(str, "^\\d+$") && (len(str) <= max);
    }

    /***
     * 判断输入字符是否由数字、字母组成
     * @param str    输入字符串
     * @return
     */
    public static boolean isAlphaNumeric(Object str) {
        return isRegex(str, "^[a-zA-Z0-9_]+$");
    }

    /***
     * 判断输入字符是否由数字、字母组成
     * @param str    输入字符串
     * @param fix    固定长度
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean isFixAlphaNumeric(Object str, int fix) {
        return isRegex(str, "^[a-zA-Z0-9_]+$") && (len(str) == fix);
    }

    /***
     * 判断输入字符是否由数字、字母组成
     * @param str    输入字符串
     * @param max    最大长度
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean isAlphaNumeric(Object str, int max) {
        return isRegex(str, "^[a-zA-Z0-9_]+$") && (len(str) <= max);
    }

    /***
     * 判断输入字符是否是版本号格式（2.0.0）
     * @param str    输入字符串
     * @return
     */
    public static boolean isVersion(Object str) {
        return isRegex(str, "^[0-9.]+$");
    }

    /***
     * 判断输入字符是否是版本号格式（2.0.0）
     * @param str    输入字符串
     * @param len    长度
     * @return
     */
    public static boolean isVersion(Object str, int len) {
        return isRegex(str, "^[0-9.]+$") && (str.toString().length() <= len);
    }

    /***
     * mac地址的判断
     * @param str 输入字符串
     * @return
     */
    public static boolean isMac(Object str) {
        return isRegex(str, "^[a-zA-Z0-9\\:]+$");
    }

    /***
     * mac地址的判断
     * @param str    输入字符串
     * @param len    长度
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean isMac(Object str, int len) {
        return isRegex(str, "^[a-zA-Z0-9\\:]+$") && (len(str) <= len);
    }

    /**
     * 检查是否为手机号码(13开头的11位数字字串)
     *
     * @param sMobile 被检查的字串
     * @return
     */
    public static boolean isMobileNumber(Object sMobile) {
        if (isEmpty(sMobile)) {
            return false;
        }
        return Pattern.matches("^1\\d{10}$", sMobile.toString());
    }

    public static boolean isEmail(Object str, int len) {
        return isRegex(str, "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$") && (len(str) <= len);
    }

    public static boolean isUrl(Object str, int len) {
        return isRegex(str, "^(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]$") && (len(str) <= len);
    }

    public static String ClobToString(Clob clob) throws SQLException, IOException {
        String reString = "";
        Reader is = clob.getCharacterStream();
        // 得到流
        BufferedReader br = new BufferedReader(is);
        String s = br.readLine();
        StringBuffer sb = new StringBuffer();
        while (s != null) {
            // 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
            sb.append(s);
            s = br.readLine();
        }
        reString = sb.toString();
        return reString;
    }

    public static long getVersion(String mmversion) {
        String[] versions = mmversion.split("\\.");
        String version = "";
        for (int i = 0; i < versions.length; i++) {
            version += String.format("%04d", Integer.valueOf(versions[i]));
        }
        if (versions.length < 2) {
            version += "000000000000";
        } else if (versions.length < 3) {
            version += "00000000";
        } else if (versions.length < 4) {
            version += "0000";
        }
        return Long.valueOf(version);
    }

    public static String getRandomId() {
        String code = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ((int) ((Math.random() * 6 + 1) * 100000));
        return code;
    }

    public static String getValue(Object value) {
        String retValue = "";
        if (value != null) {
            retValue = value.toString();
        }
        return retValue;
    }

    public static String getValue(Object value, int length) {
        String retValue = "";
        if (value != null) {
            retValue = value.toString();
            if (retValue.length() > length) {
                retValue = retValue.substring(0, length);
            }
        }
        return retValue;
    }

    /**
     * 生成uuid
     *
     * @return
     */
    public static String genUuid() {

        return UUID.randomUUID().toString();
    }

    /**
     * Truncate content. 用作打印日志
     *
     * @param content content
     * @return truncated content
     */
    public static String truncateContent(String content) {
        if (content == null) {
            return "";
        } else if (content.length() <= SHOW_CONTENT_SIZE) {
            return content;
        } else {
            return content.substring(0, SHOW_CONTENT_SIZE) + "...";
        }
    }

    private static final int SHOW_CONTENT_SIZE = 100;

    /***
     * 判断输入字符串符合某种格式
     * @param str       输入字符串
     * @param pattern   预编译好的Pattern
     * @return
     */
    public static boolean isRegex(Object str, Pattern pattern) {
        if (str == null) {
            return false;
        }
        if (isEmpty(str)) {
            str = "";
        }
        Matcher matcher = pattern.matcher(str.toString());
        return matcher.matches();
    }

}
