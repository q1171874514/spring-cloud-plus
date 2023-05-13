/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.modules.security.oauth2;

import com.example.exception.ErrorCode;
import com.example.modules.security.feign.ShiroFeign;
import com.example.user.UserDetail;
import com.example.user.UserTokenDetail;
import com.example.utils.MessageUtils;
import com.example.utils.Result;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 认证
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class Oauth2Realm extends AuthorizingRealm {
    @Autowired
    private ShiroFeign shiroFeign;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof Oauth2Token;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();

        Result<UserTokenDetail> result = shiroFeign.getByToken();
        if(result.getCode() == ErrorCode.INTERNAL_SERVER_ERROR) {
            throw new CredentialsException(result.getMsg());
        }
        //根据accessToken，查询用户信息
        UserTokenDetail userTokenDetail = result.getData();
        //token失效
        if(userTokenDetail == null || userTokenDetail.getExpireDate().getTime() < System.currentTimeMillis()){
            throw new IncorrectCredentialsException(MessageUtils.getMessage(ErrorCode.TOKEN_INVALID));
        }

        Result<UserDetail> resultUser = shiroFeign.getUser(userTokenDetail.getUserId());
        if(result.getCode() == ErrorCode.INTERNAL_SERVER_ERROR) {
            throw new UnknownAccountException(result.getMsg());
        }
        //查询用户信息
        UserDetail userDetail = resultUser.getData();


        //账号锁定
        if(userDetail.getStatus() == 0){
            throw new LockedAccountException(MessageUtils.getMessage(ErrorCode.ACCOUNT_LOCK));
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userDetail, accessToken, getName());
        return info;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserDetail user = (UserDetail)principals.getPrimaryPrincipal();

        Result<Set<String>> result = shiroFeign.getUserPermissions(user);
        if(result.getCode() == ErrorCode.INTERNAL_SERVER_ERROR) {
            throw new UnauthenticatedException(result.getMsg());
        }
        //用户权限列表
        Set<String> permsSet = shiroFeign.getUserPermissions(user).getData();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }



}