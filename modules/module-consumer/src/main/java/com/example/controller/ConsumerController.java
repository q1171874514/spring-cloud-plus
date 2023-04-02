package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {
    @Autowired
    private StreamBridge streamBridge;

//    /**
//     * 函数式编辑接收消息
//     */
//    public Consumer<String> cluster() {
//        System.out.println("接收消息");
//        return message -> {
//            System.out.println("接收的集群消息为：" + message);
//        };
//    }
//
//    /**
//     * 函数式编辑接收消息
//     */
//    @Bean
//    public Consumer<String> broadcast() {
//        return message -> {
//            System.out.println("接收的广播消息为：" + message);
//        };
//    }


}
