package com.precisource.api;

/**
 * @Author: xinput
 * @Date: 2020-06-06 14:37
 */
public class Result {

    private Integer code;

    private String message;

    public Result() {
    }

    public Result(Integer code) {
        this.code = code;
    }

    public Result(String message) {
        this.message = message;
    }

    public Result(Integer code, String message) {
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
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
