package com.dogx.core.common.annotation;

import com.dogx.core.common.enums.InvokerTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * @author zhumingjun
 * @date 2023/3/1 10:57
 * @description <TODO >
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InvokerRecord {
    InvokerTypeEnum type();

    /**
     * 调度次数
     * outside 默认10次
     * service 设置为0次，可能是请求数据异常
     * @return
     */
    int retry() default 10;
}
