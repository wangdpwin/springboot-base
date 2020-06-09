package com.precisource.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: wangdongpeng
 * @Date: 2020-06-09 15:54
 * @Description
 * @Version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD})
public @interface Authorization {
    String[] roles() default "";
    String[] permissions() default "";
}