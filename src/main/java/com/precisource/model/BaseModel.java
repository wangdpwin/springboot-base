package com.precisource.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.precisource.consts.Consts;

import java.util.Date;

/**
 * @Author: xinput
 * @Date: 2020-06-17 11:56
 */
public class BaseModel {

    @TableId
    private String id;

    private Integer recordState = Consts.RECORD_STATE_VALID;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime = new Date();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime = new Date();

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
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
