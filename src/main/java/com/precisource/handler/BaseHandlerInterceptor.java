package com.precisource.handler;

import com.bleach.ObjectId;
import com.bleach.common.StreamUtils;
import com.bleach.common.StringUtils;
import com.precisource.api.HeaderEnum;
import com.precisource.config.SystemConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 定义拦截器
 * 对请求添加requestId，方便追踪
 *
 * @Author: xinput
 * @Date: 2020-06-09 22:44
 */
@Component
public class BaseHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BaseHandlerInterceptor.class);

    private static String DEFALUT_ACCESS_CONTROL_EXPOSE_HEADERS = "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Request-Id, X-Total-Count";

    /**
     * 请求处理之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = (String) request.getAttribute(HeaderEnum.REQUEST_ID.getType());
        if (StringUtils.isNotEmpty(requestId)) {
            requestId = new StringBuffer(20).append(ObjectId.get().toString()).append("-").append(requestId).toString();
        } else {
            requestId = ObjectId.get().toString();
        }
        request.setAttribute(HeaderEnum.REQUEST_ID.getType(), requestId);
        logger.info("request 中生成的 reqeustId = {}", requestId);

        String defaultContentType = "application/json; charset=utf-8";
        //set default content type
        response.setContentType(defaultContentType);

        // default content type without utf-8
        if (response.getContentType().equalsIgnoreCase("application/json")) {
            response.setContentType(defaultContentType);
        }

        //session id
        String sessionId = request.getSession().getId();
        if (StringUtils.isNotEmpty(sessionId)) {
            response.addHeader("X-Session-Id", sessionId);
        }

        //set cors
        response.addHeader("Access-Control-Allow-Origin", "*");

        response.setHeader("Access-Control-Expose-Headers",
                "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Request-Id, X-Total-Count, X-Sample-Count");

        List<String> addHeaders = SystemConfig.getHeader();
        if (CollectionUtils.isEmpty(addHeaders)) {
            response.setHeader("Access-Control-Expose-Headers", DEFALUT_ACCESS_CONTROL_EXPOSE_HEADERS);
        } else {
            response.setHeader("Access-Control-Expose-Headers",
                    DEFALUT_ACCESS_CONTROL_EXPOSE_HEADERS + StringUtils.COMMA + StreamUtils.union(addHeaders));
        }

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PATCH, PUT, DELETE");
            response.setHeader("Access-Control-Max-Age", "86400");
        }

        // 需要返回true，否则请求不会被控制器处理
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String requestId = (String) request.getAttribute(HeaderEnum.REQUEST_ID.getType());
        logger.info("请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后），如果异常发生，则该方法不会被调用");
        logger.info("requestId = " + requestId);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
