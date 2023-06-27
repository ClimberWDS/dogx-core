package com.dogx.core.common.annotation;

import com.dogx.core.common.config.SensitiveJsonSerializer;
import com.dogx.core.common.enums.SensitiveStrategy;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 数据脱敏注解
 *
 * @author shawoo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@JacksonAnnotationsInside
@Documented
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface Sensitive {
    /**
     * 脱敏策略
     *
     * @return
     */
    SensitiveStrategy strategy();
}