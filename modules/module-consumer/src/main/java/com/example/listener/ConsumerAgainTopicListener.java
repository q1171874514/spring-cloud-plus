package com.example.listener;

import com.example.vo.ProducerVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 消费者监听器,失败重复消费
 **/
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "again-template-consumer-group", topic = "AgainTemplateTopic")
public class ConsumerAgainTopicListener implements RocketMQListener<ProducerVo> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Integer againNum = 0;
    @Override
    public void onMessage(ProducerVo message) {
        System.out.println("处理消息:" + message.getId());
        if(message.getNumber() != null && againNum > -1 && againNum >= message.getNumber()) {
            againNum = 0;
            logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
            return;
        }
        againNum ++;
        logger.error("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
        throw new RuntimeException("消费异常");
    }
}
