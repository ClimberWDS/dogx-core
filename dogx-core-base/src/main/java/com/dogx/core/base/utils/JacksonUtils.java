package com.dogx.core.base.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author shawoo
 * @create 2022-01-14 17:18
 * @desc
 */
@Slf4j
public class JacksonUtils {

    static ObjectMapper objectMapper = new ObjectMapper();

    static {

        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode toObj(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObj(String json, Class<T> cls) {
        try {
            return objectMapper.readValue(json, cls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
