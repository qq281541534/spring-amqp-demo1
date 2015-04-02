package com.busap.amqp.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Date;

/**
 * �㲥ģʽ
 * ��Ϣ�����ߣ�ֻ��Ҫ�����ŵ���������������Ϣͨ���ŵ�����������������Ҫ����
 *
 *  �󶨼�������������ת���������͡�����fanout���ͣ����Դ˲�����(routing key)
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

        //����ת����������
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = new Date().toLocaleString() + ": log something";
        //��ת����������Ϣ
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        System.out.println("[x] Sent '" + message +"'");

        channel.close();
        connection.close();
    }
}
