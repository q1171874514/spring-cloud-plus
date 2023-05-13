/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.modules.security.controller;

import com.example.modules.security.service.ShiroService;
import com.example.user.UserDetail;
import com.example.user.UserTokenDetail;
import com.example.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 权限信息
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/shiro")
@Api(tags="权限信息")
public class ShiroController {
	@Autowired
	private ShiroService shiroService;

	@PostMapping("getUserPermissions")
	@ApiOperation(value = "获取用户权限")
	public Result<Set<String>> getUserPermissions(@RequestBody UserDetail user) {

		return new Result<Set<String>>().ok(shiroService.getUserPermissions(user));
	}

	@PostMapping("getByToken")
	@ApiOperation(value = "获取登录用户信息")
	public Result<UserTokenDetail> getByToken(@RequestHeader("token") String token) {
		UserTokenDetail userTokenDetail = shiroService.getByToken(token);
		return new Result<UserTokenDetail>().ok(userTokenDetail);
	}

	@PostMapping("getUser")
	@ApiOperation(value = "获取登录用户信息")
	public Result<UserDetail> getUser(@RequestParam("userId") Long userId) {

		return new Result<UserDetail>().ok(shiroService.getUser(userId));
	}

	@PostMapping("getUserByToken")
	@ApiOperation(value = "获取登录用户信息")
	public Result<UserDetail> getUserByToken(@RequestHeader("token") String token) {

		return new Result<UserDetail>().ok(shiroService.getUserByToken(token));
	}
	
}