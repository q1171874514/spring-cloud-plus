package com.example.service.impl;

import com.example.service.RedisExpirationService;
import com.example.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisExpirationServiceImpl implements RedisExpirationService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public void redisExpiredKey(String expiredKey) {
        // 判断是否是巡查任务
        if (expiredKey.startsWith("patrol-task")) {
            // 临时key,此key可以在业务处理完，然后延迟一定时间删除，或者不处理
            String tempKey = SignUtils.md5(expiredKey, "UTF-8");
            // 临时key不存在才设置值，key超时时间为10秒（此处相当于分布式锁的应用）
            Boolean exist = redisTemplate.opsForValue().setIfAbsent(tempKey, "1", 10, TimeUnit.SECONDS);

        } else {
            log.info("Expired keys without processing");
        }
    }
}
