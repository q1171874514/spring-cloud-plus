/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.security.service.impl;

import com.example.security.service.CaptchaService;
import com.example.redis.RedisKeys;
import com.example.redis.RedisUtils;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * 验证码
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private RedisUtils redisUtils;
    @Value("${renren.redis.open: false}")
    private boolean open;
    /**
     * Local Cache  5分钟过期
     */
    Cache<String, String> localCache = CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(5, TimeUnit.MINUTES).build();

    @Override
    public BufferedImage create(String uuid) {
        Producer producer = new DefaultKaptcha();
        //生成文字验证码
        String code = producer.createText();

        //保存到缓存
        setCache(uuid, code);

        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        //获取验证码
        String captcha = getCache(uuid);

        //效验成功
        if(code.equalsIgnoreCase(captcha)){
            return true;
        }

        return false;
    }

    private void setCache(String key, String value){
        if(open){
            key = RedisKeys.getCaptchaKey(key);
            redisUtils.set(key, value, 300);
        }else{
            localCache.put(key, value);
        }
    }

    private String getCache(String key){
        if(open){
            key = RedisKeys.getCaptchaKey(key);
            String captcha = (String)redisUtils.get(key);
            //删除验证码
            if(captcha != null){
                redisUtils.delete(key);
            }

            return captcha;
        }

        String captcha = localCache.getIfPresent(key);
        //删除验证码
        if(captcha != null){
            localCache.invalidate(key);
        }
        return captcha;
    }
}