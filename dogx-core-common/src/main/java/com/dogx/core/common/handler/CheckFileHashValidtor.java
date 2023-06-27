package com.dogx.core.common.handler;

import com.dogx.core.common.annotation.CheckFileHash;
import com.dogx.core.common.utils.FileUtil;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author shawoo
 */
public class CheckFileHashValidtor implements ConstraintValidator<CheckFileHash, String> {
    @Override
    public void initialize(CheckFileHash constraintAnnotation) {
        System.out.println("constraintAnnotation initialize");
    }

    @SneakyThrows
    @Override
    public boolean isValid(String t, ConstraintValidatorContext constraintValidatorContext) {
        return FileUtil.checkFileHash(t);
    }


}