package com.precisource.interceptor;

import com.precisource.annotation.Authentication;
import com.precisource.annotation.Authorization;
import com.precisource.auth.AuthPermissionInfo;
import com.precisource.auth.AuthRoleInfo;
import com.precisource.auth.AuthService;
import com.precisource.auth.UserAuthInfo;
import com.precisource.consts.SecureConst;
import com.precisource.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.precisource.api.BaseController.forbidden;
import static com.precisource.api.BaseController.unauthorized;

/**
 * @Author: wangdongpeng
 * @Date: 2020-06-09 15:53
 * @Description
 * @Version 1.0
 */
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    private static final String TOKEN_NAME = "authorization";

    @Autowired
    private AuthService authService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isHandlerMethod = handler instanceof HandlerMethod;
        if (!isHandlerMethod) {
            return true;
        }
        Boolean isClassAuth = ((HandlerMethod) handler).getBeanType().getAnnotation(Authentication.class) == null;
        if (!isClassAuth) {
            return authRequest(request, response, handler);
        }
        Boolean isAuth = ((HandlerMethod) handler).getMethodAnnotation(Authentication.class) == null;
        if (!isAuth) {
            return authRequest(request, response, handler);
        }
        return true;
    }

    private boolean authRequest(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //放行的Uri前缀
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String target = uri.replaceFirst(contextPath, "");
        if (SecureConst.containUrl(target)) {
            return true;
        }
        //需要做token校验, 消息头的token优先于请求query参数的token
        String xHeaderToken = request.getHeader(TOKEN_NAME);
        String xRequestToken = request.getParameter(TOKEN_NAME);
        String token = null != xHeaderToken ? xHeaderToken : xRequestToken;
        if (StringUtils.isEmpty(token)) {
            unauthorized("please login or refresh token");
            return false;
        }
        UserAuthInfo userAuthInfo = authService.getUserAuthInfo(token);
        if (Objects.isNull(userAuthInfo)) {
            unauthorized("please login or refresh token");
            return false;
        }
        //判断接口权限
        String methodName = ((HandlerMethod) handler).getMethod().getName();
        String className = ((HandlerMethod) handler).getBeanType().getName();
        List<String> list = StringUtils.splitConvertToList(className, ".");
        String controllerName = list.get(list.size() - 1);
        Method m = ((HandlerMethod) handler).getMethod();
        Class<?> cls = ((HandlerMethod) handler).getBeanType();
        boolean isClzAnnotation = cls.isAnnotationPresent(Authorization.class);
        boolean isMethodAnnotation = m.isAnnotationPresent(Authorization.class);
        Authorization authorization = null;
        if (isClzAnnotation) {
            authorization = cls.getAnnotation(Authorization.class);
        } else if (isMethodAnnotation) {
            authorization = m.getAnnotation(Authorization.class);
        }
        //不需验证权限
        if (authorization == null) {
            return true;
        }
        //需要验证权限
        String[] roles = authorization.roles();
        String[] permissions = authorization.permissions();
        if (Objects.nonNull(roles)) {
            List<AuthRoleInfo> userAuthRoles = authService.getUserAuthRoles(userAuthInfo.getUserId());
            if (CollectionUtils.isEmpty(userAuthRoles)) {
                forbidden("you don't have any role, you can't access data");
                return false;
            }
            List<String> authRoles = userAuthRoles.stream().map(AuthRoleInfo::getRole).collect(Collectors.toList());
            if (!authRoles.containsAll(Arrays.asList(roles))) {
                forbidden("your roles is not enough to access data");
                return false;
            }
        }
        if (Objects.nonNull(permissions)) {
            List<AuthPermissionInfo> userAuthPermissions = authService.getUserAuthPermissions(userAuthInfo.getRoleIds());
            if (CollectionUtils.isEmpty(userAuthPermissions)) {
                forbidden("you don't have any permission, you can't access data");
                return false;
            }
            List<String> authPermissions = userAuthPermissions.stream().map(AuthPermissionInfo::getPermission).collect(Collectors.toList());
            if (!authPermissions.containsAll(Arrays.asList(permissions))) {
                forbidden("your permissionss is not enough to access data");
                return false;
            }
        }
        return true;
    }

}
