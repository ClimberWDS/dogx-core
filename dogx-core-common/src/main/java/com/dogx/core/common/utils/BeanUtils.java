package com.dogx.core.common.utils;


import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

/**
 * @author shawo
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    public static void copyNotNullProperties(Object source, Object target, String... ignoreProperties) {
        List<Field> fields = ReflectionUtils.findNotNullField(source);
        Iterator var4 = fields.iterator();

        while (var4.hasNext()) {
            try {
                Field field = (Field) var4.next();
                String fieldName = field.getName();
                boolean ignore = false;

                for (int i = 0; i < ignoreProperties.length; ++i) {
                    if (StringUtils.equals(ignoreProperties[i], fieldName)) {
                        ignore = true;
                    }
                }

                if (!ignore) {
                    try {
                        ReflectionUtils.setFieldValue(target, field.getName(), field.get(source));
                    } catch (IllegalAccessException | IllegalArgumentException var9) {
                        var9.printStackTrace();
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

}