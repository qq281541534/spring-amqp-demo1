package com.busap.springamqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Created by liuyu on 2015/3/31.
 */
@Service("messageQueueManager")
public class MessageQueueManagerImpl implements MessageQueueManager {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AmqpAdmin admin;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private SimpleMessageListenerContainer container;

    public String createQueue(String queueName, String bindingKey){
        System.out.println("createQueue..." + queueName);
        //创建队列
        Queue newQueue = new Queue(queueName);
        queueName = admin.declareQueue(newQueue);
        //将队列绑定到交换机
        admin.declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE, "topicExchange", bindingKey, new HashMap<String, Object>()));
        //把队列添加到监听器
        container.addQueues(newQueue);
        container.start();
        return queueName;
    }

    public void sendMessage(String message, String routingKey) {
        amqpTemplate.send("topicExchange", routingKey, MessageBuilder.withBody(message.getBytes()).build());
    }

    public void onMessage(Message message) {
        String msg = new String(message.getBody());
        System.out.println("receiveMessage: " + msg);
    }

}
