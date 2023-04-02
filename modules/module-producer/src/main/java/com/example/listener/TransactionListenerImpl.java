package com.example.listener;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

// 监听器,默认的为rocketMQTemplate
@RocketMQTransactionListener()
public class TransactionListenerImpl implements RocketMQLocalTransactionListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        // ... local transaction process, return rollback, commit or unknown
        System.out.println("[执行本地事务，消息：" + msg + " arg: " + arg);
        logger.info("[executeLocalTransaction][执行本地事务，消息：{} arg：{}]", msg, arg);
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        // ... check transaction status and return rollback, commit or unknown
        System.out.println("[回查消息：" + msg);
        logger.info("[checkLocalTransaction][回查消息：{}]", msg);
        return RocketMQLocalTransactionState.COMMIT;
    }

}
