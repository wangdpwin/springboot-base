package com.precisource.api;

/**
 * @Author: xinput
 * @Date: 2020-06-09 22:50
 */
public enum HeaderEnum {

    @Remark("请求id")
    REQUEST_ID("RequestId"),
    @Remark("请求开始时间")
    START_TIME("StartTime"),
    @Remark("返回总数，用于前端计算分页")
    TOTOL_COUNT("X-Total-Count");

    private String type;

    HeaderEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
