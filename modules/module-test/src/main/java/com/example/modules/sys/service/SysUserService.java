/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.modules.sys.service;


import com.example.service.BaseService;
import com.example.modules.sys.dto.SysUserDTO;
import com.example.modules.sys.entity.SysUserEntity;


/**
 * 系统用户
 * 
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends BaseService<SysUserEntity> {

	SysUserDTO get(Long id);

}
