package com.precisource.auth;

import java.util.List;
/**
 * @Author: wangdongpeng
 * @Date: 2020-06-09 16:44
 * @Description 用户登录，权限验证，必须实现该接口，返回相应用户信息
 * @Version 1.0
 */
public interface AuthService {

    public UserAuthInfo getUserAuthInfo(String token);

    public List<AuthRoleInfo> getUserAuthRoles(String userId);

    public List<AuthPermissionInfo> getUserAuthPermissions(List<String> roleIds);
}
