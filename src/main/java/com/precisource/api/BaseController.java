package com.precisource.api;

import com.bleach.common.JsonUtils;
import com.bleach.common.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * setTotalCount(long totalCount)
 * setTotalCount(String headerName, long totalCount)
 * setHeader(String headerName, String value)
 * setHeader(List<Header> headers)
 * 这四个方法在一个Mapping中只能调用一次
 */
public class BaseController {

    /**
     * ThreadLocal确保高并发下每个请求的request，response都是独立的
     */
    private ThreadLocal<HttpServletResponse> currentResponse = new ThreadLocal();

    /**
     * 线程安全初始化reque，respose对象
     */
    @ModelAttribute
    public void initReqAndRep(HttpServletResponse response) {
        currentResponse.set(response);
    }


    /**
     * 在response的header中添加X-Total-Count
     */
    protected void setTotalCount(long totalCount) {
        HttpServletResponse response = currentResponse.get();
        response.setHeader("X-Total-Count", String.valueOf(totalCount));
        currentResponse.remove();
    }

    /**
     * 在response的header中添加 headerName
     */
    protected void setTotalCount(String headerName, long totalCount) {
        HttpServletResponse response = currentResponse.get();
        response.setHeader(headerName, String.valueOf(totalCount));
        currentResponse.remove();
    }

    /**
     * 在response的header中添加 headerName
     */
    protected void setHeader(String headerName, String value) {
        HttpServletResponse response = currentResponse.get();
        response.setHeader(headerName, value);
        currentResponse.remove();
    }

    protected void setHeader(List<Header> headers) {
        HttpServletResponse response = currentResponse.get();
        if (CollectionUtils.isEmpty(headers)) {
            headers.forEach(header -> response.setHeader(header.getName(), String.valueOf(header.getValue())));
        }
        currentResponse.remove();
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
    protected void badRequest(java.lang.Error error) {
        badRequest(JsonUtils.toJsonString(error));
    }

    /**
     * Send 401 Unauthorized
     */
    protected void unauthorized(java.lang.Error error) {
        unauthorized(JsonUtils.toJsonString(error));
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
    public void forbidden(java.lang.Error error) {
        forbidden(JsonUtils.toJsonString(error));
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
    protected void error(java.lang.Error error) {
        error(JsonUtils.toJsonString(error));
    }

    /**
     * Send 509 CLIENT_OVER_QUOTA
     */
    public void limitExceeded(String message) {
        throw new BaseException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, ErrorCode.CLIENT_OVER_QUOTA, message);
    }

}
