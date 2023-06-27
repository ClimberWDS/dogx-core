package com.dogx.core.mybatis.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Auther:hxl
 * @Date:2021/10/28-10-28 9:07
 * @Version:1.0
 */

@Data
public class BaseEntity {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 操作版本号
     */
    @Version
    @TableField("op_version")
    private Integer opVersion = 0;

    /**
     * 删除状态 0:正常 1：删除*
     */
    @TableLogic
    @JsonIgnore
    private Integer deleted = 0;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
