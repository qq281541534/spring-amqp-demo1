package com.busap.amqp.dircet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * ��Ե�������Ϣ����Ҫ����routing key
 *
 * ������Ϣʱ��������routing_key�����ն�����ת�������������binding_key�������߽�����binding_key��routing_key��ͬ����Ϣ��
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
            //������Ϣ��ָ����ת������ָ��routing key
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            System.out.println("[x] Send '"+ message +"'");

        }
        channel.close();
        connection.close();

    }

    /**
     * ������־�ȼ�
     * @return
     */
    private static String getSeverity() {
        Random random = new Random();
        int val = random.nextInt(3);
        return SEVERITIES[val];
    }
}
