package com.dogx.core.base.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常
 *
 * @author shawoo
 */
@Getter
@Setter
public class CaChakenException extends RuntimeException {

    private String code;
    private String msg;
    private String details;

    public CaChakenException(String msg, String details) {
        super(msg);
        this.msg = msg;
        this.details = details;
    }

    public CaChakenException(String code, String msg, String details) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.details = details;
    }
}