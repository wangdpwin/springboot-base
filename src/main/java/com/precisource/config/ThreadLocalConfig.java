package com.precisource.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: wangdongpeng
 * @Date: 2020-06-09 13:59
 * @Description
 * @Version 1.0
 */
@Configuration
public class ThreadLocalConfig {

    @Bean
    public ThreadLocal<HttpServletResponse> getThreadLocal() {
        ThreadLocal<HttpServletResponse> threadLocal = new ThreadLocal();
        return threadLocal;
    }

}
