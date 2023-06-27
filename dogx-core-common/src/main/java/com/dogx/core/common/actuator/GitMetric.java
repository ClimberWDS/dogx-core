package com.dogx.core.common.actuator;

import cn.hutool.core.date.DateUtil;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.boot.info.GitProperties;

import java.util.Date;

public class GitMetric implements MeterBinder {
    private final GitProperties gitProperties;

    public GitMetric(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Override
    public void bindTo(MeterRegistry registry) {
        Gauge.builder("git_info", () -> 1)
                .tag("branch", gitProperties.get("branch"))
                .tag("build_host", gitProperties.get("build.host"))
                .tag("build_user_name", gitProperties.get("build.user.name"))
                .tag("build_user_email", gitProperties.get("build.user.email"))
                .tag("build_version", gitProperties.get("build.version"))
                .tag("build_time", getTime("build.time"))
                .tag("commit_user_name", gitProperties.get("commit.user.name"))
                .tag("commit_user_email", gitProperties.get("commit.user.email"))
                .tag("commit_id_abbrev", gitProperties.get("commit.id.abbrev"))
                .tag("commit_message_short", gitProperties.get("commit.message.short"))
                .tag("commit_time", getTime("commit.time"))
                .register(registry);
    }

    private String getTime(String key) {
        try {
            return DateUtil.formatDateTime(new Date(Long.parseLong(gitProperties.get(key))));
        } catch (Exception e) {
            return "";
        }
    }
}
