package com.busap.amqp.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.Random;

/**
 * 主题订阅模式接受者
 * Created by liuyu on 2015/4/1.
 */
public class TopicConsumer {

    private static final String EXCHANGE_NAME = "topic_logs";
    private static final String[] TOPICS = {"kernel.*", "auth.info", "*.critical", "kernel.#"};


    public static void main(String[] args) throws IOException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.108.166");
        factory.setPort(5672);
        factory.setUsername("mqtt_cctv");
        factory.setPassword("cn.10020.cctv");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //创建一个非持久的、唯一的、自动删除的队列
        String queueName = channel.queueDeclare().getQueue();

        String topic = getTopic();
        //接受所有与topic匹配的消息
        channel.queueBind(queueName, EXCHANGE_NAME, topic);

        System.out.println(" [*] Waiting for messages about " + topic + " To exit press CTRL+C");

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName,true,queueingConsumer);

        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String message = new String(delivery.getBody());

            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println(" [x] Received routingKey = " + routingKey
                    + ",message = " + message + ".");

        }


    }

     private static String getTopic() {
        Random random = new Random();
        int val = random.nextInt(4);
        return TOPICS[val];
    }
}
