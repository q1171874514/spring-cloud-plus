package com.example.utils;

/**
 * redis编写工具
 */
public class RedisKeyCompileUtil {

    public static String compileKey(String keyPrefix, Long id) {
        return keyPrefix + ":" + id.toString();
    }

    public static Long getKeyToId(String key) {
        String idString = key.substring(key.lastIndexOf(":") + 1, key.length());
        return Long.valueOf(idString);
    }

    public static String getKeyToPrefix(String key) {
        return key.substring(0, key.lastIndexOf(":"));
    }

}
