package com.precisource.aspect;

import static com.precisource.api.BaseController.limitExceeded;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.precisource.annotation.Limiter;
import com.precisource.consts.LimitType;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Author: wangdongpeng
 * @Date: 2020-06-09 15:05
 * @Description
 * @Version 1.0
 */
@Aspect
@Component
public class LimiterAspect {

    private static Cache<String, RateLimiter> cache = CacheBuilder.newBuilder().initialCapacity(1000)
            .expireAfterAccess(10, TimeUnit.MINUTES).build();

    private static String API_LIMIT = "10";

    private static final double DEFAULT_LIMIT = Double.valueOf(API_LIMIT);

    private static final String UNKNOWN = "unknown";

    @Pointcut("@annotation(com.precisource.annotation.Limiter)")
    public void aspect() {
    }

    @Around("execution(public * *(..)) && @annotation(com.precisource.annotation.Limiter)")
    public void interceptor(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Limiter limiter = method.getAnnotation(Limiter.class);
        LimitType limitType = limiter.limitType();
        String key;
        switch (limitType) {
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                key = limiter.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        RateLimiter rateLimiter = cache.get(key, () -> RateLimiter.create(DEFAULT_LIMIT));
        if (rateLimiter.tryAcquire()) {
            point.proceed();
        }
        limitExceeded("request much too in times");
    }

    /**
     * 获取IP地址
     * @return
     */
    public String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
