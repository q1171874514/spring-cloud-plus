package com.example.service;

public interface RedisExpirationMessageService extends RedisKeyExpirationService{
    void set(Long id, Object val);
}
