/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.redis;

/**
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public class RedisKeys {
    /**
     * 系统参数Key
     */
    public static String getSysParamsKey(){
        return "mapper.sys:params";
    }

    /**
     * 验证码Key
     */
    public static String getCaptchaKey(String uuid){
        return "mapper.sys:captcha:" + uuid;
    }

    /**
     * 登录用户Key
     */
    public static String getSecurityUserKey(Long id){
        return "mapper.sys:security:user:" + id;
    }

    /**
     * 系统日志Key
     */
    public static String getSysLogKey(){
        return "mapper.sys:log";
    }

    /**
     * 系统资源Key
     */
    public static String getSysResourceKey(){
        return  "mapper.sys:resource";
    }

    /**
     * 用户菜单导航Key
     */
    public static String getUserMenuNavKey(Long userId){
        return "mapper.sys:user:nav:" + userId;
    }

    /**
     * 用户权限标识Key
     */
    public static String getUserPermissionsKey(Long userId){
        return "mapper.sys:user:permissions:" + userId;
    }
}
