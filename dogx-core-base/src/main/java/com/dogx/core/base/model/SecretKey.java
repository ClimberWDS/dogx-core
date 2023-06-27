package com.dogx.core.base.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretKey {
    /**
     * 公钥
     */
    private PublicKey publicKey;

    /**
     * 私钥
     */
    private PrivateKey privateKey;


}
