package com.example.controller;

import com.example.entity.ProducerEntity;
import org.apache.rocketmq.common.message.MessageConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 *使用StreamBridge
 */
@RestController
@RequestMapping("/stream")
public class ProducerStreamBridgeController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private StreamBridge streamBridge;

    @GetMapping("/send")
    public boolean send() {
        String serial = UUID.randomUUID().toString();
        boolean send = streamBridge.send("erbadagang-output", MessageBuilder.withPayload(serial).build());
        System.out.println("发送消息: " + serial + (send? "成功": "失败"));
        // <4>发送消息
        return send;
    }
}
