package com.dogx.core.common.config.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.dogx.core.common.utils.DesensitizationUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @ClassName DesensitizationAppender
 * @Description 脱敏类 - 将日志进行脱敏
 * @Author shawoo
 * @Date 2021/2/14
 */
@Slf4j
public class DesensitizationAppender {
    /**
     * LoggingEvent的属性 - message
     * 格式化前的日志信息，如log.info("your name : {}", "shawoo")
     * message就是"your name : {}"
     */
    private static final String MESSAGE = "message";
    /**
     * LoggingEvent的属性 - formattedMessage
     * 格式化后的日志信息，如log.info("your name : {}", "shawoo")
     * formattedMessage就是"your name : shawoo"
     */
    private static final String FORMATTED_MESSAGE = "formattedMessage";

    public void operation(LoggingEvent event) {
        if (event.getArgumentArray() != null) {
            // 获取格式化后的Message
            String eventFormattedMessage = event.getFormattedMessage();
            DesensitizationUtil util = new DesensitizationUtil();
            // 获取替换后的日志信息
            String changeMessage = util.customChange(eventFormattedMessage);
            if (!(null == changeMessage || "".equals(changeMessage))) {
                try {
                    // 利用反射的方式，将替换后的日志设置到原event对象中去
                    Class<? extends LoggingEvent> eventClass = event.getClass();
                    // 保险起见，将message和formattedMessage都替换了
                    Field message = eventClass.getDeclaredField(MESSAGE);
                    message.setAccessible(true);
                    message.set(event, changeMessage);
                    Field formattedMessage = eventClass.getDeclaredField(FORMATTED_MESSAGE);
                    formattedMessage.setAccessible(true);
                    formattedMessage.set(event, changeMessage);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    log.error("脱敏类操作异常:{}", e.getMessage(), e);
                }
            }

        }
    }
}
