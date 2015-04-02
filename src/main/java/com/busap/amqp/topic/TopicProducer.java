package com.busap.amqp.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * 主题订阅模式的生产者
 * Created by liuyu on 2015/4/1.
 */
public class TopicProducer {

    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.108.166");
        factory.setPort(5672);
        factory.setUsername("mqtt_cctv");
        factory.setPassword("cn.10020.cctv");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String[] routing_keys = new String[]{"kernel.info", "cron.warning",
                "auth.info", "kernel.critical", "kernel.error.body"};

        for (String routing_key : routing_keys) {
            String message = UUID.randomUUID().toString();
            channel.basicPublish(EXCHANGE_NAME, routing_key, null, message.getBytes());
            System.out.println(" [x] Sent routingKey = "+routing_key+" ,message = " + message + ".");
        }

        channel.close();
        connection.close();
    }
}
