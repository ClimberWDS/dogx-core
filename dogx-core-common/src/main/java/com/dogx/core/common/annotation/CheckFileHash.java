package com.dogx.core.common.annotation;

import com.dogx.core.common.handler.CheckFileHashValidtor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 参考系统提供的校验注解可知，message，groups，payload这3个属性必须提供
 *
 * @author shawoo
 * @Constraint(validatedBy = checkFileHashValidtor.class)指定该注解的校验类
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckFileHashValidtor.class)
public @interface CheckFileHash {
    String message() default "图片hash不一致";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}