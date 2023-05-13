package com.example.service.impl;

import com.example.service.RedisExpirationMessageService;
import com.example.utils.RedisKeyCompileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisExpirationMessageServiceImpl implements RedisExpirationMessageService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public String keyPrefix() {

        return "RedisExpirationMessage";
    }

    @Override
    public void redisKeyExpiredEvent(Long id) {
        System.out.println("处理id：" + id);
    }

    @Override
    public void set(Long id, Object val) {
        redisTemplate.opsForValue().
                set(RedisKeyCompileUtil.compileKey(keyPrefix(),id),
                        val,5000, TimeUnit.MILLISECONDS);
    }
}
