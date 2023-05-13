package com.example.service;

import com.example.utils.RedisKeyCompileUtil;

public interface RedisKeyExpirationService {
    /**
     * 前缀设置
     * @return
     */
    String keyPrefix();

    /**
     * key过期时，根据前缀找到指定对象的此方法
     */
    void redisKeyExpiredEvent(Long id);

    static String compileKey(String keyPrefix, Long id) {
        return RedisKeyCompileUtil.compileKey(keyPrefix, id);
    }

    static Long getKeyToId(String key) {
        return RedisKeyCompileUtil.getKeyToId(key);
    }
}
