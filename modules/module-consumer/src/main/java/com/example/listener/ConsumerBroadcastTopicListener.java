package com.example.listener;

import com.example.entity.ProducerEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 消费者监听器广播消费
 **/
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "broadcast-template-consumer-group",
                        topic = "BroadcastTemplateTopic",
                        messageModel = MessageModel.BROADCASTING // 设置为广播消费
        )
public class ConsumerBroadcastTopicListener implements RocketMQListener<ProducerEntity> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(ProducerEntity message) {

        System.out.println("处理消息:" + message.getId());
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
