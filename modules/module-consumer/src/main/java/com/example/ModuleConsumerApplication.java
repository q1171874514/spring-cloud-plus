package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.example.**.**.feign"})
public class ModuleConsumerApplication {
    public static void main(String[] args) {

        SpringApplication.run(ModuleConsumerApplication.class, args);
    }
}
