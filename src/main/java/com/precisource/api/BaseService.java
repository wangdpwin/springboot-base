package com.precisource.api;

import com.precisource.consts.DefaultConsts;
import com.precisource.consts.ErrorCode;
import com.precisource.consts.HeaderEnum;
import com.precisource.domain.BaseHttp;
import com.precisource.exception.BaseException;
import com.precisource.util.JsonUtils;
import com.precisource.util.StringUtils;
import com.precisource.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public abstract class BaseService {

    @Autowired
    private ThreadLocal<BaseHttp> baseHttpThreadLocal;

    /**
     * 线程安全初始化 request，respose对象
     */
//    @ModelAttribute
//    public void initReqAndRep(HttpServletResponse response) {
//        currentResponse.set(response);
//    }

    /**
     * get user id in jwt token.
     *
     * @return
     */
    protected String currentUserId() {
        if (StringUtils.equalsIgnoreCase(DefaultConsts.DEFAULT_MODE, DefaultConsts.getMode())) {
            return baseHttpThreadLocal.get().getRequest().getAttribute("aud").toString();
        }
        return DefaultConsts.getMockUserId();
    }

    /**
     * 在response的header中添加X-Total-Count
     */
    protected void setTotalCount(long totalCount) {
        setTotalCount(HeaderEnum.TOTOL_COUNT.getType(), totalCount);
    }

    /**
     * 在response的header中添加 headerName
     */
    protected void setTotalCount(String headerName, long totalCount) {
        setHeader(headerName, String.valueOf(totalCount));
    }

    /**
     * 在response的header中添加 headerName
     */
    protected void setHeader(String headerName, String value) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        response.setHeader(headerName, value);
    }

    protected void setHeader(List<Header> headers) {
        HttpServletResponse response = baseHttpThreadLocal.get().getResponsen();
        if (CollectionUtils.isEmpty(headers)) {
            headers.forEach(header -> response.setHeader(header.getName(), String.valueOf(header.getValue())));
        }
    }

    /**
     * set jwt token cookie with httpOnly and secure deps on your 'application.session.secure' setting.
     *
     * @param jwt
     * @param duration like 2h, 3d
     */
    protected void setJWTCookie(String jwt, String duration) {
        Cookie cookie = new Cookie(DefaultConsts.getCookieTokenName(), jwt);
        cookie.setPath(StringUtils.SLASH);
        cookie.setSecure(DefaultConsts.getCookieSecure());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(TimeUtils.parseDuration(duration));

        baseHttpThreadLocal.get().getResponsen().addCookie(cookie);
    }

    /**
     * Send a 200 OK response
     */
    protected void ok() {
        throw new BaseException(HttpStatus.OK);
    }

    /**
     * Send 201 Created
     */
    protected void created(Object data) {
        created(JsonUtils.toJsonString(data));
    }

    /**
     * Send 201 Created
     */
    protected void created(String message) {
        throw new BaseException(HttpStatus.CREATED, message);
    }

    /**
     * Send 204 NO CONTENT
     */
    protected void noContent() {
        throw new BaseException(HttpStatus.NO_CONTENT);
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
