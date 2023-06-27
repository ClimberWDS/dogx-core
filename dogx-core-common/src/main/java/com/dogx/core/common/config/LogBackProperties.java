package com.dogx.core.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author shawoo
 * @create 2022-02-14 15:21
 * @desc
 */
@Configuration
@ConfigurationProperties(prefix = "log-desensitize")
@Data
public class LogBackProperties {
    private boolean ignore;
    private boolean open;
    private Map<String, Object> pattern;
    private List<Map<String, Object>> patterns;
}
