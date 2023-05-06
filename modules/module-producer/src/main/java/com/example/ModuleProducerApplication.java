package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableAutoDataSourceProxy
@EnableFeignClients
public class ModuleProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModuleProducerApplication.class, args);
    }
}
