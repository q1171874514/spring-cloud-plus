/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.security.service.impl;

import com.example.security.dao.SysUserTokenDao;
import com.example.security.service.ShiroService;
import com.example.modules.sys.dao.SysMenuDao;
import com.example.modules.sys.dao.SysRoleDataScopeDao;
import com.example.modules.sys.dao.SysUserDao;
import com.example.modules.sys.enums.SuperAdminEnum;
import com.example.user.UserDetail;
import com.example.user.UserTokenDetail;
import com.example.utils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private SysMenuDao sysMenuDao;
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysUserTokenDao sysUserTokenDao;
    @Autowired
    private SysRoleDataScopeDao sysRoleDataScopeDao;

    @Override
    public Set<String> getUserPermissions(UserDetail user) {
        //系统管理员，拥有最高权限
        List<String> permissionsList;
        if(user.getSuperAdmin() == SuperAdminEnum.YES.value()) {
            permissionsList = sysMenuDao.getPermissionsList();
        }else{
            permissionsList = sysMenuDao.getUserPermissionsList(user.getId());
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String permissions : permissionsList){
            if(StringUtils.isBlank(permissions)){
                continue;
            }
            permsSet.addAll(Arrays.asList(permissions.trim().split(",")));
        }

        return permsSet;
    }

    @Override
    public UserTokenDetail getByToken(String token) {

        return ConvertUtils.sourceToTarget(sysUserTokenDao.getByToken(token), UserTokenDetail.class);
    }

    @Override
    public List<Long> getDataScopeList(Long userId) {
        return sysRoleDataScopeDao.getDataScopeList(userId);
    }

    @Override
    public UserDetail getUserByToken(String token) {
        if(StringUtils.isBlank(token))
            return null;

        UserTokenDetail userTokenDetail = getByToken(token);
        if(userTokenDetail == null)
            return null;

        return getUser(userTokenDetail.getUserId());
    }

    @Override
    public UserDetail getUser(Long userId) {
        UserDetail userDetail = ConvertUtils.sourceToTarget(sysUserDao.selectById(userId), UserDetail.class);
        if(userDetail != null)
            userDetail.setDeptIdList(getDataScopeList(userId));
        return userDetail;
    }
}