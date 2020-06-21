package com.precisource.handler;

import com.auth0.jwt.JWTExpiredException;
import com.precisource.annotation.PassSecure;
import com.precisource.api.Result;
import com.precisource.config.SpringContentUtils;
import com.precisource.consts.DefaultConfig;
import com.precisource.consts.DefaultConsts;
import com.precisource.consts.ErrorCode;
import com.precisource.consts.HeaderEnum;
import com.precisource.domain.BaseHttp;
import com.precisource.exception.BaseException;
import com.precisource.util.JwtUtils;
import com.precisource.util.ObjectId;
import com.precisource.util.StreamUtils;
import com.precisource.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 定义拦截器
 * 对请求添加requestId，方便追踪
 *
 * @Author: xinput
 * @Date: 2020-06-09 22:44
 */
@Component
public class BaseHandlerInterceptor extends HandlerInterceptorAdapter {

    private static String DEFALUT_ACCESS_CONTROL_EXPOSE_HEADERS = "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Request-Id, X-Total-Count";

    @Autowired
    private ThreadLocal<BaseHttp> baseHttpThreadLocal;

//    @Autowired
//    private SpringContentUtils contentUtils;

    /**
     * 请求处理之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        baseHttpThreadLocal.set(new BaseHttp(request, response));
        setHeader(request, response);
        securtJwt(request, object);
        // 需要返回true，否则请求不会被控制器处理
        return true;
    }

    /**
     * 统一设置header参数
     *
     * @param request
     * @param response
     */
    private void setHeader(HttpServletRequest request, HttpServletResponse response) {
        String requestId = (String) request.getAttribute(HeaderEnum.REQUEST_ID.getType());
        if (StringUtils.isNotEmpty(requestId)) {
            requestId = new StringBuffer(20).append(ObjectId.get().toString()).append("-").append(requestId).toString();
        } else {
            requestId = ObjectId.get().toString();
        }
        request.setAttribute(HeaderEnum.REQUEST_ID.getType(), requestId);

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

        // request id
        response.setHeader("X-Request-Id", requestId);

        //set cors
        response.addHeader("Access-Control-Allow-Origin", "*");

        response.setHeader("Access-Control-Expose-Headers",
                "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Request-Id, X-Total-Count");

        List<String> addHeaders = DefaultConfig.getHeader();
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
    }

    /**
     * 验证token
     *
     * @param request
     * @param object
     */
    private void securtJwt(HttpServletRequest request, Object object) {
        // 如果不是映射到方法直接放行
        if (!(object instanceof HandlerMethod)) {
            return;
        }

        // 如果是dev环境，直接放行
        if (DefaultConsts.MODE.DEV.getProfile().equalsIgnoreCase(SpringContentUtils.getActiveProfile())) {
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();

        //检查是否有 PassSecure 注释，有则跳过认证
        if (method.isAnnotationPresent(PassSecure.class)) {
            PassSecure passToken = method.getAnnotation(PassSecure.class);
            if (passToken.required()) {
                return;
            }
        }

        // 如果没有注解，默认都需要token验证
        // 检查有没有需要用户权限的注解
//        if (!method.isAnnotationPresent(Secure.class)) {
//            return;
//        }

        // 从 http 请求头中取出 token
        String token = StringUtils.trim(StringUtils.substringAfter(
                request.getHeader("authorization"), "Bearer")
        );
        Result result = new Result();
        result.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_ERROR);

        if (StringUtils.isEmpty(token)) {
            unauthorized(result);
        } else {
            try {
                final Map<String, Object> claims = JwtUtils.verify(token);
                String id = String.valueOf(claims.get("aud"));
                request.setAttribute("requestLogCustomData", "[USER-" + id + "]");
                request.setAttribute("aud", id);
            } catch (Exception e) {
                if (e instanceof JWTExpiredException) {
                    result.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_TOKEN_EXPIRED);
                    unauthorized(result);
                }
                result.setCodeWithDefaultMsg(ErrorCode.CLIENT_AUTH_ERROR);
                unauthorized(result);
            }
        }
    }

    /**
     * Send 401 Unauthorized
     */
    private void unauthorized(Result result) {
        throw new BaseException(HttpStatus.UNAUTHORIZED, result.getCode(), result.getMessage());
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后），如果异常发生，则该方法不会被调用
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 清空Tomcat创建的大量session对象
        request.getSession().invalidate();
        baseHttpThreadLocal.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
