package com.busap.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by liuyu on 2015/4/1.
 */
public class NewTask {

    private final static String QUEUE_NAME = "workqueue";

    public static void main(String[] args) throws IOException {
        //��������
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.108.166");
        factory.setPort(5672);
        factory.setUsername("mqtt_cctv");
        factory.setPassword("cn.10020.cctv");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //��������
        channel.queueDeclare(QUEUE_NAME, false,false,false,null);

        //����10����Ϣ
        for (int i = 0; i < 10; i++){
            String dots = "";
            for (int j = 0; j < i; j++){
                dots += ".";
            }
            String message = "hello " + dots + dots.length();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        //�ر�Ƶ������Դ
        channel.close();
        connection.close();

    }


}
