package com.precisource.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class BaseMongoModel {

    @Id
    private String id;

    private Date createAt = new Date();

    private Date updateAt = new Date();

    private Integer recordState = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Integer getRecordState() {
        return recordState;
    }

    public void setRecordState(Integer recordState) {
        this.recordState = recordState;
    }
}
