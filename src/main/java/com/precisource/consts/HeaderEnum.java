package com.precisource.consts;

import com.precisource.annotation.Remark;

/**
 * @Author: xinput
 * @Date: 2020-06-09 22:50
 */
public enum HeaderEnum {

    @Remark("请求id")
    REQUEST_ID("X-Request-Id"),
    @Remark("Session Id")
    SESSION_ID("X-Session-Id"),
    @Remark("请求开始时间")
    START_TIME("StartTime"),
    @Remark("返回总数，用于前端计算分页")
    TOTOL_COUNT("X-Total-Count"),
    @Remark("默认返回类型")
    DEFAULT_CONTENT_TYPE("application/json; charset=utf-8");

    private String type;

    HeaderEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
