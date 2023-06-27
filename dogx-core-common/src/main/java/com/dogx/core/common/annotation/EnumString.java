package com.dogx.core.common.annotation;

import com.dogx.core.common.handler.EnumStringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 枚举参数校验注解
 * 用法：@EnumString(value={"val1","val2"})
 * @author wds
 * @date 2022-06-06 16:25
 **/
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(EnumString.List.class)
@Documented
@Constraint(validatedBy = EnumStringValidator.class)
public @interface EnumString {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value();

    /**
     * 在同一元素上定义多个 {@link EnumString} 注释
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        EnumString[] value();
    }

}
