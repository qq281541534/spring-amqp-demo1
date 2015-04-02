package com.busap.amqp.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Date;

/**
 * 广播模式
 * 消息生产者，只需要创建信道，交换机，发消息通过信道发给交换机，不需要队列
 *
 *  绑定键的意义依赖于转发器的类型。对于fanout类型，忽略此参数。(routing key)
 * Created by liuyu on 2015/4/1.
 */
public class Producer {
    private final static String EXCHANGE_NAME = "ex_log";

    public static void main(String[] args) throws IOException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.108.166");
        factory.setPort(5672);
        factory.setUsername("mqtt_cctv");
        factory.setPassword("cn.10020.cctv");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明转发器和类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = new Date().toLocaleString() + ": log something";
        //往转发器发送消息
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        System.out.println("[x] Sent '" + message +"'");

        channel.close();
        connection.close();
    }
}
