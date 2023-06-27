package com.dogx.core.mybatis.convert;

import com.dogx.core.common.model.BasePageResp;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : panxiufeng
 * @version : v1.0
 * @date : 2021/12/8 下午9:27
 * @description
 */
public class BasePageRespConvert {

    public static <T> BasePageResp<T> generate(IPage<T> iPage){
        BasePageResp<T> pageResult = new BasePageResp<>();
        pageResult.setCurrent((int)iPage.getCurrent());
        pageResult.setSize((int)iPage.getSize());
        pageResult.setPages((int)iPage.getPages());
        pageResult.setTotal((int)iPage.getTotal());
        pageResult.setRecords(iPage.getRecords());
        return pageResult;
    }

    public static <T, R> BasePageResp<R> generate(IPage<T> iPage, Function<T, R> function){
        BasePageResp<R> pageResult = new BasePageResp<>();
        pageResult.setCurrent((int)iPage.getCurrent());
        pageResult.setSize((int)iPage.getSize());
        pageResult.setPages((int)iPage.getPages());
        pageResult.setTotal((int)iPage.getTotal());
        pageResult.setRecords(iPage.getRecords().stream().map(function).collect(Collectors.toList()));
        return pageResult;
    }

}
