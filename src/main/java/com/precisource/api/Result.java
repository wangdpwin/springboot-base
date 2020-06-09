package com.precisource.api;

import java.util.List;

/**
 * @Author: xinput
 * @Date: 2020-06-08 22:35
 */
public class Result<T> {

    private long count;

    private List<T> ts;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getTs() {
        return ts;
    }

    public void setTs(List<T> ts) {
        this.ts = ts;
    }

    public Result() {
    }

    public Result(long count) {
        this.count = count;
    }

    public Result(long count, List<T> ts) {
        this.count = count;
        this.ts = ts;
    }

    public static <T> Result<T> emptyList() {
        return new Result(0L);
    }
}
