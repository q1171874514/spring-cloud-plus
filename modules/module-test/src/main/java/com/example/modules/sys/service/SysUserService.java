/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.example.modules.sys.service;


import com.example.modules.sys.dto.SysUserDTO;
import com.example.modules.sys.entity.SysUserEntity;
import com.example.service.BaseService;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends BaseService<SysUserEntity> {

    SysUserDTO get(Long id);

}
