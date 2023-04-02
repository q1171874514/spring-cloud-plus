/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.modules.security.service;

import com.example.modules.security.entity.SysUserTokenEntity;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.user.UserDetail;
import com.example.user.UserTokenDetail;

import java.util.List;
import java.util.Set;

/**
 * shiro相关接口
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(UserDetail user);

    UserTokenDetail getByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    UserDetail getUser(Long userId);

    /**
     * 获取用户对应的部门数据权限
     * @param userId  用户ID
     * @return        返回部门ID列表
     */
    List<Long> getDataScopeList(Long userId);

    /**
     * 根据token，查询用户
     * @param token
     */
    UserDetail getUserByToken(String token);

}
