package com.precisource.validate;

import com.precisource.util.Logs;
import org.slf4j.Logger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;

/**
 * @Author: xinput
 * @Date: 2020-06-19 18:27
 */
public class RequireCheck implements ConstraintValidator<Require, Object> {

    private static final Logger logger = Logs.get();

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value instanceof String) {
            return String.valueOf(value).trim().length() > 0;
        }

        if (value instanceof Collection<?>) {
            return ((Collection<?>) value).size() > 0;
        }

        if (value.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(value) > 0;
        }

        return true;
    }
}
