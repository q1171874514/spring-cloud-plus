package com.example.config;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
/**
 * redisson配置https://blog.csdn.net/bbj12345678/article/details/121144955
 *
 //单机
 RedissonClient redisson = Redisson.create();
 Config config = new Config();
 config.useSingleServer().setAddress("myredisserver:6379");
 RedissonClient redisson = Redisson.create(config);


 //主从

 Config config = new Config();
 config.useMasterSlaveServers()
 .setMasterAddress("127.0.0.1:6379")
 .addSlaveAddress("127.0.0.1:6389", "127.0.0.1:6332", "127.0.0.1:6419")
 .addSlaveAddress("127.0.0.1:6399");
 RedissonClient redisson = Redisson.create(config);


 //哨兵
 Config config = new Config();
 config.useSentinelServers()
 .setMasterName("mymaster")
 .addSentinelAddress("127.0.0.1:26389", "127.0.0.1:26379")
 .addSentinelAddress("127.0.0.1:26319");
 RedissonClient redisson = Redisson.create(config);


 //集群
 Config config = new Config();
 config.useClusterServers()
 .setScanInterval(2000) // cluster state scan interval in milliseconds
 .addNodeAddress("127.0.0.1:7000", "127.0.0.1:7001")
 .addNodeAddress("127.0.0.1:7002");
 RedissonClient redisson = Redisson.create(config);
**/
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    String redisHost;

    @Value("${spring.redis.port}")
    String redisPort;

    @Value("${spring.redis.password}")
    String redisPassword;

    @Value("${spring.redis.timeout}")
    Integer redisTimeout;

    /**
     * Redisson配置
     * @return
     */
    @Bean
    RedissonClient redissonClient() {
        //1、创建配置
        Config config = new Config();

        redisHost = redisHost.startsWith("redis://") ? redisHost : "redis://" + redisHost;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redisHost + ":" + redisPort)
                .setTimeout(redisTimeout);

        if (StringUtils.isNotBlank(redisPassword)) {
            serverConfig.setPassword(redisPassword);
        }

        return Redisson.create(config);
    }
}


