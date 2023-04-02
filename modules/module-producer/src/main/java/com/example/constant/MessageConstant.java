package com.example.constant;

/**
 * 类说明: mq 常量类
 *
 * @author wqf
 * @date 2022/9/7 9:30
 */
public interface MessageConstant {

    //生产者-集群消息主题
    public static String CLUSTER_OUTPUT="cluster-out-0";
    //生产者-广播消息主题
    public static String BROADCAST_OUTPUT="broadcast-out-0";
    //生产者-延时消息主题
    public static String DELAYED_OUTPUT="delayed-out-0";


    //消费者-集群消息主题
    public static String CLUSTER_INPUT="cluster-in-0";
    //消费者-广播消息主题
    public static String BROADCAST_INPUT="broadcast-in-0";
    //消费者-延时消息主题
    public static String DELAYED_INPUT="delayed-in-0";

}
