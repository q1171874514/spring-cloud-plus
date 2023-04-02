package com.example.controller;

import com.example.entity.ProducerEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


/**
 *使用RocketMQTemplate
 */
@RestController
@RequestMapping("/template")
@Slf4j
public class ProducerTemplateController {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private Logger logger = LoggerFactory.getLogger(getClass());

    //同步批量消息
    @GetMapping("sendBatch")
    private SendStatus sendBatch(Integer num) throws InterruptedException {
        num = (num == null || num < 1)? 1: num;
        List<Message> messages = new ArrayList<>();
        for (Integer i = 0; i < num; i++) {
            ProducerEntity producerEntity = new ProducerEntity();
            producerEntity.setId(new Random().nextLong());
            // 构建 Spring Messaging 定义的 Message 消息
            messages.add(MessageBuilder.withPayload(producerEntity).build());
        }
        /**
         * destination Topic主题
         * messages  消息
         * timeout   超时时间
         */
        SendResult sendResult = rocketMQTemplate.syncSend("TemplateTopic", messages, 30 * 1000L);
//        // 阻塞等待，保证消费
        //同步工具类
//        new CountDownLatch(1).await();
        logger.info("[sendBatch]:" + sendResult.getSendStatus());
        // 同步批量发送消息
        return sendResult.getSendStatus();
    }

    /**
     * 同步定时消息
     * @param delayLevel  时间级别
     * @return
     */
    @GetMapping("sendDelay")
    public SendStatus syncSendDelay(int delayLevel) {
        ProducerEntity producerEntity = new ProducerEntity();
        producerEntity.setId(new Random().nextLong());
        // 创建 Demo03Message 消息
        Message message = MessageBuilder.withPayload(producerEntity)
                .build();

        // 同步发送消息
        SendResult sendResult = rocketMQTemplate.syncSend("TemplateTopic",
                                                                    message, 30 * 1000,
                                                                    delayLevel);
        logger.info("[sendDelay]:" + sendResult.getSendStatus());
        return sendResult.getSendStatus();
    }
    /**
     * 异步定时消息
     * https://www.jianshu.com/p/ab1bd49bb482
     *
     * @param delayLevel  时间级别
     */
    @GetMapping("asyncSendDelay")
    public void asyncSendDelay(int delayLevel) {
        ProducerEntity producerEntity = new ProducerEntity();
        producerEntity.setId(new Random().nextLong());
        // 创建 Demo03Message 消息
        Message message = MessageBuilder.withPayload(producerEntity)
                .build();
        SendCallback callback = new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult.getSendStatus());
                logger.info("[asyncSendDelay]:" + sendResult.getSendStatus());
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println(throwable.getStackTrace());
                logger.error("[asyncSendDelay]:" + throwable.getStackTrace());
            }
        };
        // 异步发送消息
        rocketMQTemplate.asyncSend("TemplateTopic", message, callback, 30 * 1000,
                delayLevel);
    }

    //同步消息测试消费失败重新消费
    @GetMapping("sendAgain")
    private SendStatus sendAgain(Integer num){
        ProducerEntity producerEntity = new ProducerEntity();
        producerEntity.setId(new Random().nextLong());
        producerEntity.setNumber(num);
        // 创建 Demo03Message 消息
        Message message = MessageBuilder.withPayload(producerEntity)
                .build();

        SendResult sendResult = rocketMQTemplate.syncSend("AgainTemplateTopic", message);
//        // 阻塞等待，保证消费
        //同步工具类
//        new CountDownLatch(1).await();
        logger.info("[sendAgain]:" + sendResult.getSendStatus());
        // 同步批量发送消息
        return sendResult.getSendStatus();
    }

    //同步消息广播消费
    @GetMapping("sendBroadcast")
    private SendStatus sendBroadcast(Integer num){
        ProducerEntity producerEntity = new ProducerEntity();
        producerEntity.setId(new Random().nextLong());
        producerEntity.setNumber(num);
        // 创建 Demo03Message 消息
        Message message = MessageBuilder.withPayload(producerEntity)
                .build();
        SendResult sendResult = rocketMQTemplate.syncSend("BroadcastTemplateTopic", message);
        logger.info("[sendAgain]:" + sendResult.getSendStatus());
        return sendResult.getSendStatus();
    }

    //同步消息普通顺序消费
    @GetMapping("sendOrderly")
    public SendStatus syncSendOrderly(Integer num) {
        List<ProducerEntity> messages = new ArrayList<>();
        for (Integer i = 1; i <= num; i++) {
            ProducerEntity producerEntity = new ProducerEntity();
            producerEntity.setId(i.longValue());
            rocketMQTemplate.syncSendOrderly("OrderlyTemplateTopic", producerEntity, String.valueOf(1));
        }
        // 同步发送消息
        return SendStatus.SEND_OK;
    }

    //事务消息
    @GetMapping("sendTransaction")
    public SendStatus sendMessageInTransaction(Long id) {
        ProducerEntity producerEntity = new ProducerEntity();
        producerEntity.setId(id);
        // <1>创建 Demo07Message 消息
        Message<ProducerEntity> message = MessageBuilder.withPayload(producerEntity)
                .build();
        // <2>发送事务消息
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(
                "TransactionTemplateTopic",
                        message,
                        id);
        return transactionSendResult.getSendStatus();
    }

    @RocketMQTransactionListener()
    public class TransactionListenerImpl implements RocketMQLocalTransactionListener {

        private Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            // ... local transaction process, return rollback, commit or unknown
            logger.info("[executeLocalTransaction][执行本地事务，消息：{} arg：{}]", msg, arg);
            return RocketMQLocalTransactionState.UNKNOWN;
        }

        @Override
        public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
            // ... check transaction status and return rollback, commit or unknown
            logger.info("[checkLocalTransaction][回查消息：{}]", msg);
            return RocketMQLocalTransactionState.COMMIT;
        }

    }

}
