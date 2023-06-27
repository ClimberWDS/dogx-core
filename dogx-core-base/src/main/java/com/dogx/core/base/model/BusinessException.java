package com.dogx.core.base.model;

import lombok.Getter;

/**
 * 自定义异常
 *
 * @author shawoo
 */
@Getter
public class BusinessException extends RuntimeException {

    private String code;
    private String message;

    public BusinessException(ExtraCodeEnum resultEnum) {
        super(resultEnum.msg);
        this.message = resultEnum.msg;
        this.code = resultEnum.code;
    }

    public BusinessException(ExtraCodeEnum resultEnum, Throwable cause) {
        super(resultEnum.msg, cause);
        this.message = resultEnum.msg;
        this.code = resultEnum.code;
    }

    public BusinessException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}
