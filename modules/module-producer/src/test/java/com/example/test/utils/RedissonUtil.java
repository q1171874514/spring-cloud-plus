package com.example.test.utils;

import lombok.Data;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class RedissonUtil {
    @Autowired
    RedissonClient redissonClient;

    /**
     * 可重入锁
     */
    public void reentrant(String key, Runnable runnable, Integer code) {
        new Thread(() -> {
            RLock lock = redissonClient.getLock(key);
            lock.lock();
            System.out.println("抢到锁: " + code);
            runnable.run();
            System.out.println("释放锁: " + code);
            lock.unlock();
        }).start();
    }

    /**
     * 读写锁
     * bool   true写，false读
     */
    public void readWrite(String key, Boolean bool, Integer code, Runnable runnable) {
        new Thread(() -> {
            RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
            RLock rLock = bool? lock.writeLock(): lock.readLock();
            rLock.lock();
            System.out.println((bool? "抢到写锁": "抢到读锁") + "::" + code);
            runnable.run();
            System.out.println((bool? "释放写锁": "释放读锁") + "::" + code);
            rLock.unlock();
        }).start();
    }


}
