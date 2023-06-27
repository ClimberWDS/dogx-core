package com.dogx.core.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * @author zhumingjun
 * @date 2023/3/1 09:43
 * @description OMS同步业务类型
 *
 * 当前的实现是不完美的，因为补发时，需要通过feign发起，而不是反射方法
 **/
@Getter
@AllArgsConstructor
public enum InvokerTypeEnum {
    ORDER_BATCH_HANDLE(1, "受理单-办理"),
    ORDER_BATCH_CREATE(2, "受理单-创建"),
    ORDER_BATCH_UPDATE(3, "受理单-更新"),
    ORDER_BATCH_RECALL(4, "受理单-撤回"),
    ORDER_STOCK_SYNC(11, "备货单-同步"),
    ORDER_STOCK_APPLY(12, "备货单-申请"),
    ORDER_STOCK_FINISH(13, "备货单-完成"),
    ORDER_STOCK_CANCEL(14, "备货单-取消"),
    ORDER_STOCK_UPDATE(15, "备货单-更新状态"),
    ORDER_DELIVERY_UPDATE(16, "交付单-更新状态"),
    COMPANY_CREATE(21, "客户同步-创建"),
    COMPANY_UPDATE(22, "客户同步-更新"),

    ;

    private Integer code;

    private String name;

    public static InvokerTypeEnum get(Integer code) {
        for (InvokerTypeEnum e: values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static String getNameByCode(Integer code) {
        InvokerTypeEnum e = get(code);
        return e == null ? "" : e.name;
    }
}
