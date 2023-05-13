package com.example.listener;

import com.example.service.RedisKeyExpirationService;
import com.example.utils.RedisKeyCompileUtil;
import com.example.utils.RedisKeyExpirationMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 *redis过期监听器，监听过期key并发起指定RedisKeyExpirationMap方法
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    // 把我们上面一步配置的bean注入进去
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    /**
     * 针对redis数据失效事件，进行数据处理
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 用户做自己的业务处理即可,注意message.toString()可以获取失效的key
        String expiredKey = message.toString();
        String prefix = RedisKeyCompileUtil.getKeyToPrefix(expiredKey);
        RedisKeyExpirationService service = RedisKeyExpirationMap.get(prefix);
        if(service != null) {
            service.redisKeyExpiredEvent(RedisKeyCompileUtil.getKeyToId(expiredKey));
        }

    }
}
