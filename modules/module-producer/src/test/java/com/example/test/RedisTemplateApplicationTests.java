package com.example.test;

import com.example.service.RedisExpirationMessageService;
import com.example.test.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.annotation.IdempotentReceiver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@Slf4j
class RedisTemplateApplicationTests {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisExpirationMessageService redisExpirationMessageService;

    @Test
    void main() {
        System.out.println(redisTemplate);
        //向Redis存入键值对
        redisTemplate.opsForValue().set("javasss","helloworld");
        //从Redis中获取键值对
        System.out.println(redisTemplate.opsForValue().get("javasss"));
        Boolean javasss = redisTemplate.delete("javasss");

    }

    /**
     * 可重入
     */
    @Test
    void reentrant() throws InterruptedException {
        System.out.println("可重入");
        for (int i = 0; i < 20; i++) {
            int j = i;
            redissonUtil.reentrant("reentrantLock",() -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, j);
        }
        new CountDownLatch(19).await();

    }

    /**
     * 读写锁
     */
    @Test
    void readWrite() throws InterruptedException {
        System.out.println("读写锁");
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            int j = i;
            redissonUtil.readWrite("readWrite", (i % 3) == 0, j, () -> {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

    }

    /**
     * 闭锁
     * @throws InterruptedException
     */
    @Test
    void countDownLatch() throws InterruptedException {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch("countDownLatch");
        countDownLatch.trySetCount(20);
        for (int i = 0; i < 20; i++) {
            int j = i;
            new Thread(() -> {
                RCountDownLatch countDownLatch1 = redissonClient.getCountDownLatch("countDownLatch");
                System.out.println("计数扣1：" + j);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch1.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("结束");
    }

    /**
     * 信号量
     */
    @Test
    void semaphore() throws InterruptedException {
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        semaphore.release(2);
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            int j = i;
            new Thread(() -> {
                RSemaphore semaphore1 = redissonClient.getSemaphore("semaphore");
                try {
                    semaphore1.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("开始提交：" + j);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("提交结束：" + j);
                semaphore1.release();
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }

    /**
     * 信号量
     */
    @Test
    void semaphore1() throws InterruptedException {
        RSemaphore semaphore = redissonClient.getSemaphore("semaphore");
        semaphore.release(1);
        CountDownLatch countDownLatch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++) {
            int j = i;
            new Thread(() -> {
                RSemaphore semaphore1 = redissonClient.getSemaphore("semaphore");
                if(semaphore1.tryAcquire()) {
                    System.out.println("開始提交" + j);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("正在提交" + j);
                }
                semaphore1.release();
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
    }

    /**
     * 提交幂等
     */
    @Test
    void idempotent() {
        RLock lock = redissonClient.getLock("submit");
        final Boolean[] bool = {false};
        for (int i = 0; i < 20; i++) {
            Integer j = i;
            new Thread(() -> {
                if(!bool[0] && lock.tryLock()) {
                    System.out.println("开始提交：" + j);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        System.out.println("完成提交：" + j);
                        bool[0] = true;
                        lock.unlock();
                    }


                } else if (!bool[0]) {
                    System.out.println("正在提交，不可重复提交：" + j);
                } else {
                    System.out.println("已经提交，不可重复提交" + j);
                }


            }).start();
        }
    }

    @Test
    public void keyExpiration() {
        // 优惠券信息
        String id = "2023021685264735";
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("amount", "1000");
        map.put("type", "1001");
        map.put("describe", "满减红包");
        System.out.println("发起优惠券信息");
        // 缓存到redis
        redisTemplate.opsForHash().putAll("com.mall.coupon.id." + id, map);
        // 设置过期时间
        redisTemplate.expire("com.mall.coupon.id." + id, 10, TimeUnit.SECONDS);
    }

    @Test
    public void redisExpirationMessage() {
        Long id = Long.valueOf(222212430);
        redisExpirationMessageService.set(id, "ssssss");
    }
}
