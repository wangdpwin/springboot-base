package com.precisource.auth;

import java.util.List;

/**
 * @Author: wangdongpeng
 * @Date: 2020-06-09 16:45
 * @Description
 * @Version 1.0
 */
public class UserAuthInfo {
    private String userId;
    private List<String> roleIds;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
