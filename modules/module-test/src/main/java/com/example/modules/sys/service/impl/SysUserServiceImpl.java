/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.example.modules.sys.service.impl;

import com.example.service.impl.BaseServiceImpl;
import com.example.modules.sys.dao.SysUserDao;
import com.example.modules.sys.dto.SysUserDTO;
import com.example.modules.sys.entity.SysUserEntity;

import com.example.modules.sys.service.SysUserService;
import com.example.utils.ConvertUtils;

import org.springframework.stereotype.Service;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {


    @Override
    public SysUserDTO get(Long id) {
        SysUserEntity entity = baseDao.getById(id);

        return ConvertUtils.sourceToTarget(entity, SysUserDTO.class);
    }


}
