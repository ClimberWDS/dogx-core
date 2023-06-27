package com.dogx.core.common.utils;

import java.util.UUID;

public class UuidUtils {

    public static String getShortString() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
