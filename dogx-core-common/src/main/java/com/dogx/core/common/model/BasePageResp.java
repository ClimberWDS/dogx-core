package com.dogx.core.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author panxf
 * @createTime 2021年12月4日 20:39
 */
@Data
public class BasePageResp<T> implements Serializable {

    /**
     * 当前页
     */
    private int current;

    /**
     * 每页的数量
     */
    private int size;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 总记录数
     */
    private int total;

    /**
     * 结果集
     */
    private List<T> records;

}
