package com.busap.amqp.dircet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * 点对点生产消息，需要设置routing key
 *
 * 发送消息时可以设置routing_key，接收队列与转发器间可以设置binding_key，接收者接收与binding_key与routing_key相同的消息。
 * Created by liuyu on 2015/4/1.
 */
public class DirectProducer {

    private final static String EXCHANGE_NAME = "ex_logs_direct";
    private final static String[] SEVERITIES = {"warning", "info", "error"};

    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.108.166");
        factory.setPort(5672);
        factory.setUsername("mqtt_cctv");
        factory.setPassword("cn.10020.cctv");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        for (int i = 0; i < 6; i++){
            String severity = getSeverity();
            String message = severity + "_log :" + UUID.randomUUID().toString();
            //发布消息至指定的转发器，指定routing key
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            System.out.println("[x] Send '"+ message +"'");

        }
        channel.close();
        connection.close();

    }

    /**
     * 随意日志等级
     * @return
     */
    private static String getSeverity() {
        Random random = new Random();
        int val = random.nextInt(3);
        return SEVERITIES[val];
    }
}
