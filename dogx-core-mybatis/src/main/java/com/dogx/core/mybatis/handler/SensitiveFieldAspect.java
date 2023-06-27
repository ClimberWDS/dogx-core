//package com.dogx.core.mybatis.handler;
//
//import com.dogx.core.base.model.ChakenException;
//import com.dogx.core.base.model.ExtraCodeEnum;
//import com.dogx.core.common.annotation.SM4Encrypt;
//import com.dogx.core.common.enums.EncryptTypeEnums;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Base64Utils;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.Parameter;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//
///**
// * 敏感字段解密/加密切面
// *
// * @Author: yangp
// * @date: 2020/4/29 9:30
// */
//@Component
//@Aspect
//@Slf4j
//public class SensitiveFieldAspect {
//
//    @Value("${file.url:}")
//    private String fileUrl;
//
//    /*    @Value("${cmp.keynum.num}")
//    private Integer cipherKeyNum;
//    @Value("${cmp.keynum.sm4key}")
//    private String sm4key;*/
//
//    @Value("${cmp.keynum.sm4num:}")
//    private Integer sm4Num;
//
//    @Pointcut("execution(* com.dogx..*.mapper.*.*(..))")
//    public void pointCut() {
//
//    }
//
//    @Around("pointCut()")
//    public Object doProcess(ProceedingJoinPoint pjp) throws Throwable {
//        Object[] args = pjp.getArgs();
//        if (sm4Num==null) {
//            throw new ChakenException(ExtraCodeEnum.ERROR_PARAMS.getCode(), "缺少sm4key配置");
//        }
//        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
//        for (int i = 0; i < method.getParameters().length; i++) {
//            Parameter parameter = method.getParameters()[i];
//            SM4Encrypt annotation = parameter.getAnnotation(SM4Encrypt.class);
//            if (annotation == null) {
//                continue;
//            }
//            Object object = args[i];
//            Class<?> clazz = parameter.getType();
//            Field[] fields = clazz.getDeclaredFields();
//            //参数字符串
//            if (object instanceof String && annotation != null) {
//                if (EncryptTypeEnums.URL.equals(annotation.type())) {
//                    if (StringUtils.isBlank(fileUrl)) {
//                        throw new ChakenException(ExtraCodeEnum.ERROR_PARAMS.getCode(), "缺少file.url配置");
//                    }
//                    args[i] = args[i].toString().replace(fileUrl, "");
//                    return pjp.proceed(pjp.getArgs());
//                }
//                if (StringUtils.isNotBlank(args[i].toString())) {
//                    args[i] = Base64Utils.encodeToString(CmpSmClientUtil.sm4.encrypt(args[i].toString(), sm4Num));
//                }
//                //return pjp.proceed(pjp.getArgs());
//            } else if (object instanceof Map && annotation != null) {
//                Map<Object, Object> map = (Map) object;
//                if (map != null && map.size() > 0) {
//                    if (map.containsKey("phoneNumber") && Objects.nonNull(map.get("phoneNumber")) && !"".equals(map.get("phoneNumber"))) {
//                        map.put("phoneNumber", Base64Utils.encodeToString(CmpSmClientUtil.sm4.encrypt(map.get("phoneNumber").toString(),sm4Num)));
//                    }
//                    if (map.containsKey("identityNo") && Objects.nonNull(map.get("identityNo")) && !"".equals(map.get("identityNo"))) {
//                        map.put("identityNo", Base64Utils.encodeToString(CmpSmClientUtil.sm4.encrypt(map.get("identityNo").toString(),sm4Num)));
//                    }
//                }
//            } else if (verifyObject(object)) {
//                //封装类型
//                changeAttributeValue(fields, object);
////                //对父类属性敏感数据加密 脱敏
//                Class<?> superclass = clazz.getSuperclass();
//                if (Objects.nonNull(superclass)) {
//                    Field[] superClassFields = superclass.getDeclaredFields();
//                    changeAttributeValue(superClassFields, object);
//                }
//                //参数对象
//            }
//        }
//        return pjp.proceed(args);
//    }
//
//    /**
//     * 更改属性值
//     */
//    private void changeAttributeValue(Field[] fields, Object object) throws Exception {
//        if (fields != null && fields.length > 0) {
//            for (int j = 0; j < fields.length; j++) {
//                SM4Encrypt annotation = fields[j].getAnnotation(SM4Encrypt.class);
//                //属性上是否存在SM4Encrypt标签
//                if (annotation != null) {
//                    String a = "", b;
//                    Field f = a.getClass().getDeclaredField("value");
//                    f.setAccessible(true);
//                    Method m = object.getClass().getMethod("get" + getMethodName(fields[j].getName()));
//                    a = (String) m.invoke(object);
//                    if (StringUtils.isNotEmpty(a)) {
//                        if (EncryptTypeEnums.URL.equals(annotation.type())) {
//                            b = a.replace(fileUrl, "");
//                        } else {
//                            b = Base64Utils.encodeToString(CmpSmClientUtil.sm4.encrypt(a, sm4Num));
//                        }
//                        char[] ch = b.toCharArray();
//                        //重置加密数据
//                        f.set(a, ch);
//                    }
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 首字母大写
//     *
//     * @param filedName
//     * @return
//     * @throws Exception
//     */
//    private static String getMethodName(String filedName) {
//        byte[] items = filedName.getBytes();
//        items[0] = (byte) ((char) items[0] - 'a' + 'A');
//        return new String(items);
//    }
//
//    /**
//     * 校验是否对象类型
//     *
//     * @param object
//     * @return
//     */
//    private static boolean verifyObject(Object object) {
//        if (object == null) {
//            return false;
//        }
//        if (object instanceof Integer) {
//            return false;
//        } else if (object instanceof Long) {
//            return false;
//        } else if (object instanceof Short) {
//            return false;
//        } else if (object instanceof Boolean) {
//            return false;
//        } else if (object instanceof Byte) {
//            return false;
//        } else if (object instanceof Character) {
//            return false;
//        } else if (object instanceof Double) {
//            return false;
//        } else if (object instanceof Float) {
//            return false;
//        } else if (object instanceof String) {
//            return false;
//        } else if (List.class.isAssignableFrom(object.getClass())) {
//            return false;
//        } else if (object instanceof Map) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//}
