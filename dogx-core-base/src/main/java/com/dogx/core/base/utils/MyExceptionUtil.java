package com.dogx.core.base.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyExceptionUtil {
    /**
     * 获取异常详细信息，知道出了什么错，错在哪个类的第几行 .
     *
     * @param e
     * @return
     */
    public static String getExceptionDetail(Throwable e) {
        StringBuffer bf = new StringBuffer();
        bf.append("异常信息：");
        bf.append(e.getMessage());
        //页面换行
        bf.append("<br/>");
        StackTraceElement[] stackTraces = e.getStackTrace();
        int i = 0;
        for (StackTraceElement trace : stackTraces) {
            i++;
            bf.append(trace.toString()).append("<br/>");
            //最多打印20行就可以了
            if (i > 20) {
                break;
            }
        }
        return bf.toString();
    }
}