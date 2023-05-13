package com.example.service;

public interface RedisExpirationService {
    String REDISCODE = "patrol-task";
    void redisExpiredKey(String expiredKey);
}
