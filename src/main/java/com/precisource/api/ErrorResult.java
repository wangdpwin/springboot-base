package com.precisource.api;

/**
 * @Author: xinput
 * @Date: 2020-06-06 14:37
 */
public class ErrorResult {

    private Integer code;

    private String message;

    public ErrorResult() {
    }

    public ErrorResult(Integer code) {
        this.code = code;
    }

    public ErrorResult(String message) {
        this.message = message;
    }

    public ErrorResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
