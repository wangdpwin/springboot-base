package com.precisource.annotation;

import com.precisource.consts.LimitType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: wangdongpeng
 * @Date: 2020-06-09 15:03
 * @Description
 * @Version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Limiter {
    /**
     * 资源的名字
     *
     * @return String
     */
    String name() default "";

    /**
     * 资源的key
     *
     * @return String
     */
    String key() default "";

    /**
     * Key的prefix
     *
     * @return String
     */
    String prefix() default "";

    /**
     * 给定的时间段
     * 单位秒
     *
     * @return int
     */
    int period();

    /**
     * 最多的访问限制次数
     *
     * @return int
     */
    int count();

    /**
     * 类型
     *
     * @return LimitType
     */
    LimitType limitType() default LimitType.CUSTOMER;
}
