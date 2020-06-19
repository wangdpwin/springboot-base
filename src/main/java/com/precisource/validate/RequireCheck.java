package com.precisource.validate;

import com.precisource.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: xinput
 * @Date: 2020-06-19 18:27
 */
public class RequireCheck implements ConstraintValidator<Require, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNullOrEmpty(value);
    }
}
