package com.precisource.api;

import com.precisource.config.DefaultConfig;
import com.precisource.consts.ErrorCode;
import com.precisource.domain.BaseHttp;
import com.precisource.exception.BaseException;
import com.precisource.util.StringUtils;
import com.precisource.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
public abstract class BaseService {

    @Autowired
    private ThreadLocal<BaseHttp> baseHttpThreadLocal;

    /**
     * set jwt token cookie with httpOnly and secure deps on your 'application.session.secure' setting.
     *
     * @param jwt
     * @param duration like 2h, 3d
     */
    protected void setJWTCookie(String jwt, String duration) {
        Cookie cookie = new Cookie(DefaultConfig.getCookieTokenName(), jwt);
        cookie.setPath(StringUtils.SLASH);
        cookie.setSecure(DefaultConfig.getCookieSecure());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(TimeUtils.parseDuration(duration));

        baseHttpThreadLocal.get().getResponsen().addCookie(cookie);
    }

    /**
     * Send 400 bad request
     */
    protected void badRequestIfNull(Object object, String message) {
        if (object == null) {
            badRequest(message);
        }
    }

    /**
     * Send 400 bad request
     */
    protected void badRequest() {
        badRequest("Bad request");
    }

    /**
     * Send 400 bad request
     */
    protected void badRequest(String message) throws BaseException {
        throw new BaseException(HttpStatus.BAD_REQUEST, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, message);
    }

    /**
     * Send 400 bad request
     */
    protected void badRequest(Result result) {
        throw new BaseException(HttpStatus.BAD_REQUEST, result.getCode(), result.getMessage());
    }

    /**
     * Send 401 Unauthorized
     */
    protected void unauthorized(Result result) {
        throw new BaseException(HttpStatus.UNAUTHORIZED, result.getCode(), result.getMessage());
    }

    /**
     * Send 401 Unauthorized
     */
    public void unauthorized(String message) {
        throw new BaseException(HttpStatus.UNAUTHORIZED, ErrorCode.CLIENT_AUTH_ERROR, message);
    }

    /**
     * Send 401 Unauthorized
     */
    public void unauthorized() {
        throw new BaseException(HttpStatus.UNAUTHORIZED, ErrorCode.CLIENT_AUTH_ERROR);
    }

    /**
     * Send 403 forbidden
     */
    public void forbidden() {
        throw new BaseException(HttpStatus.FORBIDDEN, ErrorCode.CLIENT_ACCESS_DENIED);
    }

    /**
     * Send 403 forbidden
     */
    public void forbidden(String message) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, ErrorCode.CLIENT_OVER_QUOTA, message);
    }

    /**
     * Send 403 forbidden
     */
    public void forbidden(Result result) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, result.getCode(), result.getMessage());

    }

    /**
     * Send 404 bad request
     */
    protected void notFound(String message) {
        throw new BaseException(HttpStatus.NOT_FOUND, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, message);
    }

    /**
     * Send 404 bad request
     */
    protected void notFound() {
        notFound(StringUtils.EMPTY);
    }

    /**
     * Send 404 bad request
     */
    protected void notFoundIfNull(Object o) {
        if (o == null) {
            notFound();
        }
    }

    /**
     * Send 404 bad request
     */
    protected void notFoundIfNull(Object o, String message) {
        if (o == null) {
            notFound(message);
        }
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected void error() {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SERVER_INTERNAL_ERROR);
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected void error(String message) {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SERVER_INTERNAL_ERROR, message);
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected void error(Result result) {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, result.getCode(), result.getMessage());

    }

    /**
     * Send 509 CLIENT_OVER_QUOTA
     */
    public void limitExceeded(String message) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, ErrorCode.CLIENT_OVER_QUOTA, message);
    }

}
