package com.precisource.validate;

import com.precisource.consts.BaseConsts;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: xinput
 * @Date: 2020-06-29 11:55
 */
public class GenderCheck implements ConstraintValidator<Gender, Object> {

    @Override
    public void initialize(Gender gender) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null || value.toString().length() == 0) {
            return true;
        }

        String gender = String.valueOf(value);
        return BaseConsts.FEMALE.equalsIgnoreCase(gender)
                || BaseConsts.FEMALE.equalsIgnoreCase(gender);
    }
}
