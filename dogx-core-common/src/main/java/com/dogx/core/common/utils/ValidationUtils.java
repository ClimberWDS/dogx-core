package com.dogx.core.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static final String MOBILE_REGEX = "^1(3|4|5|6|7|8|9)\\d{9}$";
    public static final String ID_CARD_REGEX = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
//    public static final String EMAIL_REGEX = "^[A-Za-z0-9\\u4e00-\\u9fa5_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 是否是邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(final String str) {
        return str.matches("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$");
    }

    /**
     * 是否是手机号
     *
     * @param str
     * @return
     */
    public static boolean isMobile(final String str) {
        // "^[1][3,4,5,7,8][0-9]{9}$"
        Pattern p = Pattern.compile(MOBILE_REGEX);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 是否是座机号
     *
     * @param str 座机号码
     * @return
     */
    public static boolean isTelephone(final String str) {
        String regex = "^0\\d{2,3}\\d{7,8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        boolean b = m.matches();
        return b || isTelephoneBranch(str);
    }

    /**
     * 是否带分机号
     *
     * @param str 座机号码
     * @return
     */
    public static boolean isTelephoneBranch(final String str) {
        String regex = "^0\\d{2,3}\\d{7,8}\\d{1,5}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * 是否是身份证号
     *
     * @param str
     * @return
     */
    public static boolean isIdCard(final String str) {
        // 验证身份证
        Pattern p = Pattern.compile(ID_CARD_REGEX);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 验证是否是强密码
     *
     * @param password
     * @return
     */
    public static boolean isStrongPwd(String password) {
        String str = "/\\^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}\\$/";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }


    /**
     * 密码8到16为
     *
     * @param password
     * @return
     */
    public static boolean isPwdLength(String password) {
        String regex = "^\\d{8,16}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        return m.matches();
    }

}
