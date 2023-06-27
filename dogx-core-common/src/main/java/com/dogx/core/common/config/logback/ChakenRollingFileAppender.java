package com.dogx.core.common.config.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * @ClassName RollingFileAppenderDS
 * @Description
 * @Author shawoo
 * @Date 2021/2/14
 */
public class ChakenRollingFileAppender extends RollingFileAppender {

    @Override
    protected void subAppend(Object event) {
        DesensitizationAppender appender = new DesensitizationAppender();
        appender.operation((LoggingEvent)event);
        super.subAppend(event);
    }
}
