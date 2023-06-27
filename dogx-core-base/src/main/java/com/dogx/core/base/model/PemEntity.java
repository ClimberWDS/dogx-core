package com.dogx.core.base.model;

import lombok.Data;

/**
 * @Auther:hxl
 * @Date:2021/8/18-08-18 17:34
 * @Version:1.0
 */
@Data
public class PemEntity {
    /***
     * 自有的签名证书
     */
    private  String selfSignCert;
    /***
     * 自有的加密证书
     */
    private String selfEncryptCert;
    /***
     * 服务管理分系统加密证书
     */
    private String centerEncryptCert;
    /***
     * 服务管理分系统签名证书
     */
    private String centerSignCert;
}
