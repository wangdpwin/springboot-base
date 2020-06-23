package com.precisource.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 验证参数不能为空，且不允许为空字符串
 *
 * @Author: xinput
 * @Date: 2020-06-19 17:00
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RequireCheck.class)
public @interface Require {

    String message() default "params is empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
