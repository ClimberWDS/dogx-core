package com.dogx.core.common.annotation;

import com.dogx.core.common.enums.EncryptTypeEnums;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * 描述：sm4参数加密
 *
 * @author yangp.
 * @date 2021/6/1.
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface SM4Encrypt {
    /**
     * 默认SM4
     * url: 图片  返回添加域名,存库去掉域名前缀
     *
     * @return
     */
    EncryptTypeEnums type() default EncryptTypeEnums.SM4;
}
