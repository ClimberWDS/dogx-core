package com.dogx.core.base.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther:hxl
 * @Date:2021/11/8-11-08 23:18
 * @Version:1.0
 */
@Data
public class CertInfoEntity implements Serializable {
    private String certInfo;
    private String publicKey;
    private String subject;
    private Integer version;
    private Long serialNo;
    private String signKey;
    private String issuer;
    private String sigAlgName;
    private String sigAlgOid;
    private String signature;
    private Date effectiveTime;
    private Date expireTime;
    private String tBSCertificate;
}
