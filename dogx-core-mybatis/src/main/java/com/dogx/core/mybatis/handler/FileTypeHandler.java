package com.dogx.core.mybatis.handler;

import com.dogx.core.base.model.BusinessException;
import com.dogx.core.base.model.ExtraCodeEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 描述：
 *
 * @author yangp.
 * @date 2021/6/1.
 */

@Slf4j
@Component
public class FileTypeHandler extends BaseTypeHandler {
    @Value("${file.url:}")
    private String fileUrl;

    @SneakyThrows
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) {
        try {
            String enc = "";
            //如果存在多个手机号码以逗号（,）分割，分开加密再组合
            if (Objects.nonNull(parameter) && parameter.toString().contains(",") && !(parameter.toString().startsWith("{") && parameter.toString().endsWith("}"))) {
                List<String> strings = Arrays.asList(parameter.toString().split(","));
                for (String string : strings) {
                    if (StringUtils.isEmpty(enc)) {
                        enc = encodeFileUrl(string);
                    } else {
                        enc += "," + encodeFileUrl(string);
                    }
                }
                ps.setString(i, enc);
            } else {
                if (parameter != null) {
                    enc = encodeFileUrl(parameter.toString());
                    ps.setString(i, enc);
                } else {
                    ps.setString(i, null);
                }
            }
        } catch (Exception e) {
            log.error("[BaseTypeHandler]加密异常", e);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            if (StringUtils.isBlank(rs.getString(columnName))) {
                return rs.getString(columnName);
            } else {
                String decrypt = "";
                String encData = rs.getString(columnName);
                if (encData.contains(",")) {
                    List<String> strings = Arrays.asList(encData.split(","));
                    for (String string : strings) {
                        if (StringUtils.isEmpty(decrypt)) {
                            decrypt = decodeFileUrl(string);
                        } else {
                            decrypt += "," + decodeFileUrl(string);
                        }
                    }
                } else {
                    decrypt = decodeFileUrl(rs.getString(columnName));
                }
                return decrypt;
            }
        } catch (Exception e) {
            log.error("数据库sm4解密失败：{}", e.getMessage(), e);
            return rs.getString(columnName);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            if (StringUtils.isBlank(rs.getString(columnIndex))) {
                return rs.getString(columnIndex);
            } else {
                String decrypt = "";
                String encData = rs.getString(columnIndex);
                if (encData.contains(",")) {
                    List<String> strings = Arrays.asList(encData.split(","));
                    for (String string : strings) {
                        if (StringUtils.isEmpty(decrypt)) {
                            decrypt = decodeFileUrl(string);
                        } else {
                            decrypt += "," + decodeFileUrl(string);
                        }
                    }
                } else {
                    decrypt = decodeFileUrl(rs.getString(columnIndex));
                }
                return decrypt;
            }
        } catch (Exception e) {
            log.error("数据库sm4解密失败：{}", e.getMessage(), e);
            return rs.getString(columnIndex);
        }
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            if (StringUtils.isBlank(cs.getString(columnIndex))) {
                return cs.getString(columnIndex);
            }
            String encData = cs.getString(columnIndex);
            return getNullableResult(encData);
        } catch (Exception e) {
            log.error("数据库sm4解密失败：{}", e.getMessage(), e);
            return cs.getString(columnIndex);
        }
    }

    private String getNullableResult(String encData) {
        String decrypt = "";
        if (encData.contains(",")) {
            List<String> strings = Arrays.asList(encData.split(","));
            for (String string : strings) {
                if (StringUtils.isEmpty(decrypt)) {
                    decrypt = decodeFileUrl(string);
                } else {
                    decrypt += "," + decodeFileUrl(string);
                }
            }
        } else {
            decrypt = decodeFileUrl(encData);
        }
        return decrypt;
    }


    private String encodeFileUrl(String text) {
        if (StringUtils.isBlank(fileUrl)) {
            throw new BusinessException(ExtraCodeEnum.ERROR_PARAMS.code, "缺少file.url配置");
        }
        return text.replace(fileUrl, "");
    }


    private String decodeFileUrl(String text) {

        if (StringUtils.isBlank(fileUrl)) {
            throw new BusinessException(ExtraCodeEnum.ERROR_PARAMS.code, "缺少file.url配置");
        }
        if (text.startsWith("http://") || text.startsWith("https://")) {
            return text;
        }
        return fileUrl + text;

    }
}
