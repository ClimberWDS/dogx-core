package com.dogx.core.common.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author panxf
 * @Description 分页参数
 * @createTime 2021年11月29日 14:39
 */
@Data
public class BasePageReq implements Serializable {

    /**
     * 当前页
     */
    @Min(1)
    private long pageIndex = 1;

    /**
     * 每页的数量
     */
    @Min(1)
    @Max(1000)
    private long pageSize = 10;


    public static void main(String[] args) throws JsonProcessingException {
        BasePageReq basePageReq = new BasePageReq();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(basePageReq);
    }
}
