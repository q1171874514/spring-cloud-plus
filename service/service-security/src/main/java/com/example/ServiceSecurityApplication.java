package com.example;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.spring.context.annotation.config.EnableNacosConfig;
import com.example.modules.security.config.ShiroConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties
public class ServiceSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSecurityApplication.class, args);
    }
}
