package com.precisource.model;

/**
 * 基础筛选条件
 *
 * @Author: xinput
 * @Date: 2020-06-20 12:22
 */
public class BaseCondition {

    /**
     * 关键字查询
     */
    private String keyworkds;

    /**
     * 偏移量
     */
    private Integer offset;

    /**
     * 数据条数
     */
    private Integer limit;

    public String getKeyworkds() {
        return keyworkds;
    }

    public void setKeyworkds(String keyworkds) {
        this.keyworkds = keyworkds;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
