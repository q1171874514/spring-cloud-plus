package com.example.modules.security.feign.factory;

import com.example.modules.security.feign.ShiroFeign;
import com.example.user.UserDetail;
import com.example.user.UserTokenDetail;
import com.example.utils.Result;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ShiroFallbackFactory implements FallbackFactory<ShiroFeign> {
    @Override
    public ShiroFeign create(Throwable throwable) {
        return new ShiroFeign() {
            @Override
            public Result<Set<String>> getUserPermissions(UserDetail user) {
                return new Result<Set<String>>().error("获取权限失败，请重试");
            }

            @Override
            public Result<UserTokenDetail> getByToken() {
                return new Result<UserTokenDetail>().error("获取token信息失败，请重试");
            }

            @Override
            public Result<UserDetail> getUser(Long userId) {
                return new Result<UserDetail>().error("获取用户信息失败，请重试");
            }

            @Override
            public Result<UserDetail> getUserByToken() {
                return new Result<UserDetail>().error("获取用户信息失败，请重试");
            }
        };
    }
}
