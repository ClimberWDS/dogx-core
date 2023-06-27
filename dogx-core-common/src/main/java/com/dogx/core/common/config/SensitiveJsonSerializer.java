package com.dogx.core.common.config;

import com.dogx.core.common.annotation.Sensitive;
import com.dogx.core.common.enums.SensitiveStrategy;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.io.IOException;
import java.util.Objects;

/**
 * 序列化注解自定义实现
 * JsonSerializer<String>：指定String 类型，serialize()方法用于将修改后的数据载入
 *
 * @author shawoo
 */
@RefreshScope
public class SensitiveJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private SensitiveStrategy strategy;

    @Value("${file.url:}")
    private String fileUrl;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            //对URL作兼容
            if (StringUtils.isNotBlank(value) &&  SensitiveStrategy.URL.equals(this.strategy) && !value.startsWith("http://") && !value.startsWith("https://")) {
                gen.writeString(this.strategy.desensitizer().apply(this.fileUrl + value));
            } else {
                gen.writeString(this.strategy.desensitizer().apply(value));
            }

    }

    /**
     * 获取属性上的注解属性
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {

        Sensitive annotation = property.getAnnotation(Sensitive.class);
        if (Objects.nonNull(annotation) && Objects.equals(String.class, property.getType().getRawClass())) {
            this.strategy = annotation.strategy();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);

    }
}