package com.dogx.core.common.config;

import com.dogx.core.common.actuator.GitMetric;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(ProjectInfoAutoConfiguration.class)
public class ActuatorConfig {

    @Bean
    @ConditionalOnResource(resources = "git.properties")
    public GitMetric gitMetric(GitProperties gitProperties) {
        return new GitMetric(gitProperties);
    }
}
