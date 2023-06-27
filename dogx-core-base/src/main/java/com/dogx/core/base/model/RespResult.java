package com.dogx.core.base.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther:hxl
 * @Date:2021/9/26-09-26 16:24
 * @Version:1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespResult<T> implements Serializable {
    private final static int SUCCESS_STATUS = 1;
    private final static int FAIL_STATUS = -1;
    private int status;
    private String errorCode;
    private String errorMsg;
    private T data;

    public static <T> RespResult<T> success() {
        return success(null);
    }

    public static <T> RespResult<T> success(T data) {
        return RespResult.<T>builder().status(SUCCESS_STATUS).errorCode(ExtraCodeEnum.NORMAL.code).errorMsg(ExtraCodeEnum.NORMAL.msg).data(data).build();
    }

    public static <T> RespResult<T> fail(String code, String msg) {
        return RespResult.<T>builder().status(FAIL_STATUS).errorCode(code).errorMsg(msg).build();
    }

    public static <T> RespResult<T> fail(ExtraCodeEnum extraCode) {
        return fail(extraCode.code,extraCode.msg);
    }

    public static <T>RespResult<T> fail(ExtraCodeEnum extraCode, T data){
        return RespResult.<T>builder().status(FAIL_STATUS).errorCode(extraCode.code).errorMsg(extraCode.msg).data(data).build();
    }

    public boolean result() {
        return status == SUCCESS_STATUS;
    }
}
