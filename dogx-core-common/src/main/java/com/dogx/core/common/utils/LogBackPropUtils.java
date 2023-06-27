package com.dogx.core.common.utils;

import com.dogx.core.common.config.LogBackProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.DumperOptions;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @ClassName YmlUtils
 * @Description Yml配置文件操作相关
 * @Author shawoo
 * @Date 2022/2/14
 */
@Component
public class LogBackPropUtils {
    @Autowired
    private LogBackProperties logBackProperties;
    private static LogBackProperties staticServiceConfig;
    /**
     * key:patterns对应key下的规则Key
     */
    public static final String CUSTOM = "custom";
    public static final DumperOptions OPTIONS = new DumperOptions();


    @PostConstruct
    public void init() {
        staticServiceConfig = logBackProperties;
        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
    }

    /**
     * 获取key为pattern的值
     *
     * @return pattern对应的map，或者null（如pattern=123这种情况）
     */
    public static Map<String, Object> getPattern() {
        return staticServiceConfig.getPattern();
    }

    /**
     * 获取所有pattern，含key为pattern，key为patterns
     *
     * @return pattern
     */
    public static Map<String, Object> getAllPattern() {
        Map<String, Object> allPattern = new HashMap<>();
        Map<String, Object> pattern = getPattern();
        Map<String, Object> patterns = getPatterns();
        if (!CollectionUtils.isEmpty(patterns)) {
            allPattern.putAll(patterns);
        }
        // 注意：patterns中的key与pattern的key重复，patterns中的不生效（Map无重复Key）
        if (!CollectionUtils.isEmpty(pattern)) {
            allPattern.putAll(pattern);
        }
        return allPattern;
    }

    /**
     * 获取key为patterns的值
     *
     * @return patterns对应的map，或者null（如patterns=123这种情况）
     */
    public static Map<String, Object> getPatterns() {
        Map<String, Object> map = new HashMap<>();
        Object patterns = staticServiceConfig.getPatterns();
        // patterns下有多个key的时候(List)
        if (patterns instanceof List) {
            // 获取key为"patterns"的值(List<Map<String, Object>>)
            List<Map<String, Object>> list = (List<Map<String, Object>>) patterns;
            if (!CollectionUtils.isEmpty(list)) {
                Iterator<Map<String, Object>> iterator = list.iterator();
                // 黄线强迫症，用for代替while
                for (; iterator.hasNext(); ) {
                    Map<String, Object> maps = iterator.next();
                    assembleMap(map, maps);
                }
                return map;
            }
        }
        // patterns只有一个key的时候，且非List
        if (patterns instanceof Map) {
            assembleMap(map, (Map<String, Object>) patterns);
            return map;
        } else {
            return null;
        }
    }

    /**
     * 将patterns中每个key对应的规则按<key,规则>的方式放入map
     *
     * @param map      map
     * @param patterns patterns
     */
    private static void assembleMap(Map<String, Object> map, Map<String, Object> patterns) {
        // 获取patterns里key值为"key"的值(脱敏关键字)
        Object key = patterns.get("key");
        if (key instanceof String) {
            // 清除空格
            String keyWords = ((String) key).replace(" ", "");
            // 以逗号分隔出一个key数组
            String[] keyArr = keyWords.split(",");
            for (String keyStr : keyArr) {
                Object o = patterns.get(CUSTOM);
                if (o instanceof Map) {
                    Map<String, Object> param = (Map) o;
                    if (param.size() > 0) {
                        List<Object> list = new ArrayList<>();
                        for (Map.Entry<String, Object> entry : param.entrySet()) {
                            list.add(entry.getValue());
                        }
                        map.put(keyStr, list);
                    } else {
                        map.put(keyStr, o);
                    }
                } else {
                    map.put(keyStr, o);
                }
            }
        }
    }

    /**
     * 是否开启脱敏，默认不开启
     *
     * @return 是否开启脱敏
     */
    public static Boolean getOpen() {
        if (staticServiceConfig != null) {
            return staticServiceConfig.isOpen();
        } else {
            return false;
        }
    }

    /**
     * 是否忽略大小写匹配，默认开启
     *
     * @return 是否忽略大小写匹配
     */
    public static Boolean getIgnore() {
        if (staticServiceConfig != null) {
            return staticServiceConfig.isIgnore();
        } else {
            return false;
        }
    }
}
