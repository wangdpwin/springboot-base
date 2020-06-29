package com.precisource.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.precisource.consts.BaseConsts;

import java.time.LocalDateTime;

/**
 * @Author: xinput
 * @Date: 2020-06-17 11:56
 */
public class BaseModel {

    @TableId
    private String id;

    @JsonIgnore
    private Integer recordState = BaseConsts.RECORD_STATE_VALID;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * @TableField(value = "update_time", update = "now()")
     * 因为采用了mybatis plus的selectOne，所以查询出来的对象是有一个明确时间了，然后update的时候就会注入这个原时间。
     * 所以配置在mysql上的ON UPDATE CURRENT_TIMESTAMP就不会生效
     */
    @TableField(value = "update_time", update = "now()")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime = LocalDateTime.now();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRecordState() {
        return recordState;
    }

    public void setRecordState(Integer recordState) {
        this.recordState = recordState;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "id='" + id + '\'' +
                ", recordState=" + recordState +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
