package com.precisource.handler;

import com.precisource.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xinput
 * @Date: 2020-06-09 10:33
 */
@Component
public class AddResponseHeaderFilter extends OncePerRequestFilter {

    @Autowired
    private ThreadLocal<HttpServletResponse> threadLocal;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        threadLocal.set(httpServletResponse);
        String defaultContentType = "application/json; charset=utf-8";
        //set default content type
        httpServletResponse.setContentType(defaultContentType);

        // default content type without utf-8
        if (httpServletResponse.getContentType().equalsIgnoreCase("application/json")) {
            httpServletResponse.setContentType(defaultContentType);
        }

        //session id
        String sessionId = request.getSession().getId();
        if (StringUtils.isNotEmpty(sessionId)) {
            httpServletResponse.addHeader("X-Session-Id", sessionId);
        }

        // request id
        String requestId = request.getHeader("id");
        if (StringUtils.isNotEmpty(requestId)) {
            httpServletResponse.setHeader("X-Request-Id", requestId);
        }

        //set cors
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            httpServletResponse.setHeader("Access-Control-Allow-Headers",
                    "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Request-Id, X-Total-Count");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, PATCH, PUT, DELETE");
            httpServletResponse.setHeader("Access-Control-Max-Age", "86400");
        } else {
            httpServletResponse.setHeader("Access-Control-Expose-Headers",
                    "Origin, Authorization, Content-Type, If-Match, If-Modified-Since, If-None-Match, If-Unmodified-Since, Accept-Encoding, X-Request-Id, X-Total-Count");
        }

        filterChain.doFilter(request, httpServletResponse);
    }

}
