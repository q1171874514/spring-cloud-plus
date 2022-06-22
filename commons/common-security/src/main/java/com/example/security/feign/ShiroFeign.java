package com.example.security.feign;


import com.example.security.feign.configuration.FeignConfiguration;
import com.example.security.feign.factory.ShiroFallbackFactory;
import com.example.user.UserDetail;
import com.example.user.UserTokenDetail;
import com.example.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

// 填入注册中心中的应用名, 也就是要调用的微服务的应用名
@FeignClient(name = "service-security", configuration = {FeignConfiguration.class},fallbackFactory = ShiroFallbackFactory.class)
@RequestMapping("/shiro")
public interface ShiroFeign {

    @PostMapping("getUserPermissions")
    Result<Set<String>> getUserPermissions(@RequestBody UserDetail user);

    @PostMapping("getByToken")
    Result<UserTokenDetail> getByToken();

    @PostMapping("getUser")
    Result<UserDetail> getUser(@RequestParam("userId") Long userId);

    @PostMapping("getUserByToken")
    Result<UserDetail> getUserByToken();

}