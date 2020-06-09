package com.precisource.api;

import com.precisource.consts.ErrorCode;
import com.precisource.exception.BaseException;
import com.precisource.util.JsonUtils;
import com.precisource.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BaseController {

    @Autowired
    private ThreadLocal<HttpServletResponse> threadLocal;

    protected void setTotalCount(long totalCount) {
        threadLocal.get().setHeader("x-totalCount",String.valueOf(totalCount));
    }

    /**
     * Send a 200 OK response
     */
    protected static void ok() {
        throw new BaseException(HttpStatus.OK);
    }

    /**
     * Send 201 Created
     */
    protected static void created(Object data) {
        created(JsonUtils.toJsonString(data));
    }

    /**
     * Send 201 Created
     */
    protected static void created(String message) {
        throw new BaseException(HttpStatus.CREATED, message);
    }

    /**
     * Send 204 NO CONTENT
     */
    protected static void noContent() {
        throw new BaseException(HttpStatus.NO_CONTENT);
    }

    /**
     * Send 400 bad request
     */
    protected static void badRequestIfNull(Object object, String message) {
        if (object == null) {
            badRequest(message);
        }
    }

    /**
     * Send 400 bad request
     */
    protected static void badRequest() {
        badRequest("Bad request");
    }

    /**
     * Send 400 bad request
     */
    protected static void badRequest(String message) throws BaseException {
        throw new BaseException(HttpStatus.BAD_REQUEST, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, message);
    }

    /**
     * Send 400 bad request
     */
    protected static void badRequest(java.lang.Error error) {
        badRequest(JsonUtils.toJsonString(error));
    }

    /**
     * Send 401 Unauthorized
     */
    protected static void unauthorized(java.lang.Error error) {
        unauthorized(JsonUtils.toJsonString(error));
    }

    /**
     * Send 401 Unauthorized
     */
    protected static void unauthorized(String message) {
        throw new BaseException(HttpStatus.UNAUTHORIZED, ErrorCode.CLIENT_AUTH_ERROR, message);
    }

    /**
     * Send 401 Unauthorized
     */
    protected static void unauthorized() {
        throw new BaseException(HttpStatus.UNAUTHORIZED, ErrorCode.CLIENT_AUTH_ERROR);
    }

    /**
     * Send 404 bad request
     */
    protected static void notFound(String message) {
        throw new BaseException(HttpStatus.NOT_FOUND, ErrorCode.CLIENT_RESOURCE_NOT_FOUND, message);
    }

    /**
     * Send 404 bad request
     */
    protected static void notFound() {
        notFound(StringUtils.EMPTY);
    }

    /**
     * Send 404 bad request
     */
    protected static void notFoundIfNull(Object o) {
        if (o == null) {
            notFound();
        }
    }

    /**
     * Send 404 bad request
     */
    protected static void notFoundIfNull(Object o, String message) {
        if (o == null) {
            notFound(message);
        }
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected static void error() {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SERVER_INTERNAL_ERROR);
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected static void error(String message) {
        throw new BaseException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.SERVER_INTERNAL_ERROR, message);
    }

    /**
     * Send 500 INTERNAL_SERVER_ERROR
     */
    protected static void error(java.lang.Error error) {
        error(JsonUtils.toJsonString(error));
    }

    /**
     * set total count header.
     *
     * @param totalCount
     */
    protected static void setTotalCount(Object totalCount) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(totalCount));
    }
}
