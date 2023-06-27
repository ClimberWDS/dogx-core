package com.dogx.core.common.utils;

/**
 * @author panxf
 * @create 2022/1/14 9:49
 * @desc
 */
public class CmsLoginUserHandlerUtil {

    private static ThreadLocal<String> CURRENT_LOGIN_USER_ID = new ThreadLocal<>();

    public static void setLoginUserId(String userId) {
        CURRENT_LOGIN_USER_ID.set(userId);
    }

    public static String getLoginUserId() {
        return CURRENT_LOGIN_USER_ID.get() == null ? "unknown" : CURRENT_LOGIN_USER_ID.get();
    }

    public static void removeLoginUserId() {
        CURRENT_LOGIN_USER_ID.remove();
    }
}
