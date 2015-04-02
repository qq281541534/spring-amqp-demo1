package com.busap.springamqp;


import org.springframework.amqp.core.MessageListener;

/**
 * Created by liuyu on 2015/3/31.
 */
public interface MessageQueueManager extends MessageListener {

    String createQueue(String queueName, String bandingKey);

    void sendMessage(String message, String destinationQueueName);
}
