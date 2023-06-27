package com.dogx.core.base.model;

import lombok.Getter;

/**
 * 自定义异常
 *
 * @author shawoo
 */
@Getter
public class CmsGwChakenException extends RuntimeException {

    private String code;
    private String message;

    public CmsGwChakenException(ExtraCodeEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMsg();
    }

    public CmsGwChakenException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}