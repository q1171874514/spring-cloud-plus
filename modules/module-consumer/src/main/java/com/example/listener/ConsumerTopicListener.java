package com.example.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.vo.ProducerVo;
/**
 * 消费者监听器
 **/
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "template-consumer-group", topic = "TemplateTopic")
public class ConsumerTopicListener implements RocketMQListener<ProducerVo> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(ProducerVo message) {

        System.out.println("处理消息:" + message.getId());
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }
}
