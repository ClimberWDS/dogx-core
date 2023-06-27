package com.dogx.core.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.Random;

public class VerifyCodeUtil {

    private static  final  String SOURCE = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGJKLZXCVBNM1234567890";

    public static String get4Code(){
        return StringUtils.leftPad(new Random().nextInt(10000) + "", 4, "0");
    }

    public static String get6Code(){
        return StringUtils.leftPad(new Random().nextInt(1000000) + "", 6, "0");
    }

    public static String get6Mixed(){
        StringBuilder result = new StringBuilder();
        int size = SOURCE.length();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(size);
            char c = SOURCE.charAt(index);
            result.append(c);
        }
        return result.toString();
    }
}
