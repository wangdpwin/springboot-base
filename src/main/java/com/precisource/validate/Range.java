package com.precisource.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Author: xinput
 * @Date: 2020-06-20 18:21
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RangeCheck.class)
public @interface Range {

    String message() default "value shoud between min and max";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return size the element must be higher or equal to
     */
    double min() default Double.MIN_VALUE;

    /**
     * @return size the element must be lower or equal to
     */
    double max() default Double.MAX_VALUE;
}
