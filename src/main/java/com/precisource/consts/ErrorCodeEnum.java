package com.precisource.consts;

import com.precisource.util.StringUtils;

/**
 * @Author: xinput
 * @Date: 2020-06-08 16:53
 */
public enum ErrorCodeEnum {
    /**
     * generic code
     */
    UNKNOWN(-1, "未知错误"),
    SUCCESS(0, "成功"),

    /*
     * 客户端错误.
     */

    CLIENT_FORMAT_ERROR(1100, "消息格式错误"),
    CLIENT_AUTH_ERROR(1200, "身份验证失败"),
    CLIENT_AUTH_TOKEN_EXPIRED(1210, "身份令牌过期"),
    CLIENT_TIMEOUT(1300, "1300"),
    CLIENT_ACCESS_DENIED(1400, "访问被拒绝"),
    CLIENT_TIMEOUT_LOCKED(1500, "客户端超时退出"),
    CLIENT_RESOURCE_NOT_FOUND(2100, "找不到资源"),
    CLIENT_CREDIT_LOWER_LIMIT(2400, "余额不足"),
    CLIENT_OVER_QUOTA(2500, "超过配额"),

    /*
     * 服务端错误
     */
    SERVER_INTERNAL_ERROR(5000, "服务器内部错误"),
    SERVER_BUSY(5100, "服务器繁忙"),
    SERVER_RESOURCE_LIMIT(5200, "资源不足"),
    SERVER_UPDATE(5300, "服务更新中");

    private final int code;

    private final String desc;

    ErrorCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getMsg(Integer code) {
        if (code == null) {
            return StringUtils.EMPTY;
        }

        for (ErrorCodeEnum codeEnum : ErrorCodeEnum.values()) {
            if (String.valueOf(codeEnum.getCode()).equalsIgnoreCase(String.valueOf(code))) {
                return codeEnum.getDesc();
            }
        }

        return StringUtils.EMPTY;
    }
}
