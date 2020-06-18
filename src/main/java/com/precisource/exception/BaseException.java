package com.precisource.exception;

import org.springframework.http.HttpStatus;

/**
 * @Author: xinput
 * @Date: 2020-06-06 22:50
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -6005972502557129121L;

    private HttpStatus httpStatus;

    private Integer code;

    private String message;

    public BaseException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public BaseException(HttpStatus httpStatus, Integer code) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = super.getMessage();
    }

    public BaseException(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public BaseException(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
