package com.example.initializing;

import com.example.service.RedisKeyExpirationService;
import com.example.utils.RedisKeyExpirationMap;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 初始化继承RedisKeyExpirationService接口实体类对象，保存到RedisKeyExpirationMap中
 */
@Component
public class RedisKeyExpirationInitializing implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        putRedisKeyToMethod(applicationContext);
    }

    private void putRedisKeyToMethod(ApplicationContext applicationContext) {
        //获取绑定RedisKeyExpirationService的实现类
        Map<String, RedisKeyExpirationService> beansOfType =
                applicationContext.getBeansOfType(RedisKeyExpirationService.class);
        //插入到过期map当中
        beansOfType.values().forEach(service ->  {
            RedisKeyExpirationMap.put(service.keyPrefix(), service);
        });
    }
}
