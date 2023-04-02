package com.example;

//import com.example.source.ProducerSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.example.**.**.feign"})
public class ModuleProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModuleProducerApplication.class, args);
    }
}
