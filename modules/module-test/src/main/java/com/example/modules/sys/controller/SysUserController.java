/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.modules.sys.controller;



import com.example.modules.sys.dto.SysUserDTO;
import com.example.modules.sys.service.SysUserService;
import com.example.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
@Api(tags="用户管理")
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	@GetMapping("{id}")
	@ApiOperation("信息")
	@RequiresPermissions("sys:user:info")
	public Result<SysUserDTO> get(@PathVariable("id") Long id){
		SysUserDTO data = sysUserService.get(id);

		return new Result<SysUserDTO>().ok(data);
	}
}