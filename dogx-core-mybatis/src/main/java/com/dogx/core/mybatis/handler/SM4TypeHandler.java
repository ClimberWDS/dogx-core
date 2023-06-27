//package com.dogx.core.mybatis.handler;
//
//
//import com.dogx.core.base.model.ChakenException;
//import com.dogx.core.base.model.ExtraCodeEnum;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Base64Utils;
//
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
///**
// * 描述：
// *
// * @author yangp.
// * @date 2021/6/1.
// */
//
//@Slf4j
//@Component
//public class SM4TypeHandler extends BaseTypeHandler {
//
//    @Value("${cmp.keynum.sm4num:}")
//    private Integer sm4Num;
//
//    @SneakyThrows
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) {
//        try {
//            String enc = "";
//            //如果存在多个手机号码以逗号（,）分割，分开加密再组合
//            if (Objects.nonNull(parameter) && parameter.toString().contains(",") && !(parameter.toString().startsWith("{") && parameter.toString().endsWith("}"))) {
//                List<String> strings = Arrays.asList(parameter.toString().split(","));
//                for (String string : strings) {
//                    if (StringUtils.isEmpty(enc)) {
//                        enc = encodeSm4(string);
//                    } else {
//                        enc += "," + encodeSm4(string);
//                    }
//                }
//                ps.setString(i, enc);
//            } else {
//                if (parameter != null) {
//                    enc = encodeSm4(parameter.toString());
//                    ps.setString(i, enc);
//                } else {
//                    ps.setString(i, null);
//                }
//            }
//        } catch (Exception e) {
//            log.error("[BaseTypeHandler]加密异常", e);
//        }
//    }
//
//    @Override
//    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        try {
//            if (StringUtils.isBlank(rs.getString(columnName))) {
//                return rs.getString(columnName);
//            } else {
//                String decrypt = "";
//                String encData = rs.getString(columnName);
//                if (encData.contains(",")) {
//                    List<String> strings = Arrays.asList(encData.split(","));
//                    for (String string : strings) {
//                        if (StringUtils.isEmpty(decrypt)) {
//                            decrypt = decodeSm4(string);
//                        } else {
//                            decrypt += "," + decodeSm4(string);
//                        }
//                    }
//                } else {
//                    decrypt = decodeSm4(rs.getString(columnName));
//                }
//                return decrypt;
//            }
//        } catch (Exception e) {
//            log.error("数据库sm4解密失败：{}", e.getMessage(), e);
//            return rs.getString(columnName);
//        }
//    }
//
//    @Override
//    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        try {
//            if (StringUtils.isBlank(rs.getString(columnIndex))) {
//                return rs.getString(columnIndex);
//            } else {
//                String decrypt = "";
//                String encData = rs.getString(columnIndex);
//                if (encData.contains(",")) {
//                    List<String> strings = Arrays.asList(encData.split(","));
//                    for (String string : strings) {
//                        if (StringUtils.isEmpty(decrypt)) {
//                            decrypt = decodeSm4(string);
//                        } else {
//                            decrypt += "," + decodeSm4(string);
//                        }
//                    }
//                } else {
//                    decrypt = decodeSm4(rs.getString(columnIndex));
//                }
//                return decrypt;
//            }
//        } catch (Exception e) {
//            log.error("数据库sm4解密失败：{}", e.getMessage(), e);
//            return rs.getString(columnIndex);
//        }
//    }
//
//    @Override
//    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
//        try {
//            if (StringUtils.isBlank(cs.getString(columnIndex))) {
//                return cs.getString(columnIndex);
//            }
//            String encData = cs.getString(columnIndex);
//            return getNullableResult(encData);
//        } catch (Exception e) {
//            log.error("数据库sm4解密失败：{}", e.getMessage(), e);
//            return cs.getString(columnIndex);
//        }
//    }
//
//    private String getNullableResult(String encData) throws Exception {
//        String decrypt = "";
//        if (encData.contains(",")) {
//            List<String> strings = Arrays.asList(encData.split(","));
//            for (String string : strings) {
//                if (StringUtils.isEmpty(decrypt)) {
//                    decrypt = decodeSm4(string);
//                } else {
//                    decrypt += "," + decodeSm4(string);
//                }
//            }
//        } else {
//            decrypt = decodeSm4(encData);
//        }
//        return decrypt;
//    }
//
//
//    private String encodeSm4(String text) {
//        if (sm4Num == null) {
//            throw new ChakenException(ExtraCodeEnum.ERROR_PARAMS.code, "缺少sm4Num配置");
//        }
//        return Base64Utils.encodeToString(CmpSmClientUtil.sm4.encrypt(text, sm4Num));
//    }
//
//
//    private String decodeSm4(String text) {
//        if (sm4Num == null) {
//            throw new ChakenException(ExtraCodeEnum.ERROR_PARAMS.code, "缺少sm4key配置");
//        }
//        return new String(CmpSmClientUtil.sm4.decrypt(org.bouncycastle.util.encoders.Base64.decode(text), sm4Num));
//    }
//
//}
