package com.example.utils;

import com.example.service.RedisKeyExpirationService;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * redis过期key绑定的处理对象
 */
public class RedisKeyExpirationMap {
    static final Map<String, RedisKeyExpirationService> redisKeyExpirationMap = new HashMap<>();
    public static void put(String key, RedisKeyExpirationService service) {
        redisKeyExpirationMap.put(key, service);
    }
    public static RedisKeyExpirationService get(String key) {
        return redisKeyExpirationMap.get(key);
    }
}
