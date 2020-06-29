package com.precisource.validate;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * @author zanxus
 * @version 1.0.0
 * @date 2018-04-18 14:57
 * @description
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = GenderCheck.class)
public @interface Gender {
    String message() default "validation.belong";
}
