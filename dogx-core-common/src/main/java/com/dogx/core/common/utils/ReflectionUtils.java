package com.dogx.core.common.utils;

import com.dogx.core.common.annotation.ExtendParent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Slf4j
public class ReflectionUtils {
    private static final Map<String, Field> FieldCache = Collections.synchronizedMap(new WeakHashMap());
    private static final Map<Class, Object> FieldsCache = Collections.synchronizedMap(new WeakHashMap());
    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    public ReflectionUtils() {
    }

    public static List<Field> findNotNullField(Object obj) {
        Class c = obj.getClass();
        ArrayList list = new ArrayList();
        try {
            List<Field> fields = findAllField(c);
            Iterator var4 = fields.iterator();
            while (var4.hasNext()) {
                Field f = (Field) var4.next();
                f.setAccessible(true);
                if (f.get(obj) != null) {
                    list.add(f);
                }
            }
            return list;
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }

    public static List<Field> findAllField(Class c) {
        return findAllField(c, (List) null);
    }

    public static List<Field> findAllField(Class c, List<String> filterFields) {
        List<Field> fs = (List) FieldsCache.get(c);
        if (fs != null && fs.size() != 0) {
            return fs;
        } else {
            ArrayList list = new ArrayList();

            try {
                Class parent = c.getSuperclass();

                Field[] superfields2;
                int var9;
                for (boolean hasAnnotation = parent.isAnnotationPresent(ExtendParent.class); hasAnnotation; hasAnnotation = parent.isAnnotationPresent(ExtendParent.class)) {
                    ExtendParent annotation = (ExtendParent) parent.getAnnotation(ExtendParent.class);
                    if (annotation.extendParentField()) {
                        superfields2 = parent.getDeclaredFields();
                        Field[] var8 = superfields2;
                        var9 = superfields2.length;

                        for (int var10 = 0; var10 < var9; ++var10) {
                            Field f = var8[var10];
                            if (filterFields == null || !filterFields.contains(f.getName())) {
                                list.add(f);
                            }
                        }
                    }

                    parent = parent.getSuperclass();
                }

                Field[] fields = c.getDeclaredFields();
                superfields2 = fields;
                int var14 = fields.length;

                for (var9 = 0; var9 < var14; ++var9) {
                    Field f = superfields2[var9];
                    if (filterFields == null || !filterFields.contains(f.getName())) {
                        list.add(f);
                    }
                }
            } catch (Exception var12) {
                throw new RuntimeException(var12);
            }

            FieldsCache.put(c, list);
            return list;
        }
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        } else if (Modifier.isFinal(field.getModifiers())) {
            log.trace("final变量，不做修改" + field.getName());
        } else {
            try {
                field.set(obj, value);
            } catch (IllegalAccessException var5) {
                log.error("setFieldValue error:{}", var5.getMessage());
            }

        }
    }

    public static Field getAccessibleField(Object obj, String fieldName) {
        Assert.notNull(obj, "object不能为空");
        Assert.hasText(fieldName, "fieldName");
        Class superClass = obj.getClass();

        while (superClass != Object.class) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException var4) {
                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }


}