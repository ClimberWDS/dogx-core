package com.dogx.core.common.utils;

import org.springframework.util.StringUtils;

/**
 * @author
 * @createTime 2021-09-11 10:05
 * @desc 数据脱敏工具类
 */

public class DataMaskingUtils {

    /**
     * 手机号格式校验正则
     */
    private static final String PHONE_REGEX = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9]|9[0-9])\\d{8}$";

    /**
     * 手机号脱敏筛选正则
     */
    private static final String PHONE_BLUR_REGEX = "(\\d{3})\\d{4}(\\d{4})";

    /**
     * 手机号脱敏替换正则
     */
    private static final String PHONE_BLUR_REPLACE_REGEX = "$1****$2";

    /**
     * 身份证校验正则
     */
    private static final String ID_CARD_REGEX = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

    /**
     * 姓名校验正则
     */
    private static final String NAME_REGEX = "^[\\u4E00-\\u9FA5]+(·[\\u4E00-\\u9FA5]+)*$";

    /**
     * 手机号格式校验
     *
     * @param phone
     * @return
     */
    private static boolean checkPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return ValidationUtils.isMobile(phone);
    }

    /**
     * 身份证格式校验
     *
     * @param idCard
     * @return
     */
    private static boolean checkIdCard(String idCard) {
        if (StringUtils.isEmpty(idCard)) {
            return false;
        }
        return idCard.matches(ID_CARD_REGEX);
    }

    /**
     * 姓名校验
     *
     * @param name
     * @return
     */
    private static boolean checkName(String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
//        return name.matches(NAME_REGEX);
        return true;
    }

    /**
     * 脱敏手机号
     *
     * @param mobile
     * @return
     */
    public static String mobileMasking(String mobile) {
        if (!checkPhone(mobile)) {
            return mobile;
        }
        return mobile.replaceAll(PHONE_BLUR_REGEX, PHONE_BLUR_REPLACE_REGEX);
    }

    /**
     * 脱敏身份证号
     * 前6后4
     *
     * @param idCard
     * @return
     */
    public static String idCardMasking(String idCard) {
        if (checkIdCard(idCard)) {
            if (idCard.length() == 15) {
                return idCard.substring(0, 6) + "*****" + idCard.substring(11, 15);
            }
            if (idCard.length() == 18) {
                return idCard.substring(0, 6) + "********" + idCard.substring(14, 18);
            }
        }
        return idCard;
    }

    /**
     * 姓名脱敏
     * 一位时不脱敏，两位时'名'脱敏，两位以上保留首位中间脱敏
     *
     * @param name
     * @return
     */
    public static String nameMasking(String name) {
        if (!checkName(name)) {
            return name;
        }
        return toNameMasking(name);
    }

    /**
     * 姓名脱敏
     * 一位时不脱敏，两位时'名'脱敏，两位以上保留首位中间脱敏
     *
     * @param name
     * @return
     */
    private static String toNameMasking(String name) {
        char[] chars = name.toCharArray();
        if (chars.length == 2) {
            return chars[0] + "*";
        } else if (chars.length > 2) {
            for (int i = 1; i < chars.length - 1; i++) {
                chars[i] = '*';
            }
            return new String(chars);
        }
        return name;
    }
}