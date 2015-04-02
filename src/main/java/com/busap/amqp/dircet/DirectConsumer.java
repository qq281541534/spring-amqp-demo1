package com.busap.amqp.dircet;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.Random;

/**
 * ��Ե����Ѷ� ������Binding key
 *
 * ������Ϣʱ��������routing_key�����ն�����ת�������������binding_key�������߽�����binding_key��routing_key��ͬ����Ϣ��
 * Created by liuyu on 2015/4/1.
 */
public class DirectConsumer {

    private final static String EXCHANGE_NAME = "ex_logs_direct";
    private final static String[] SEVERITIES = {"warning", "info", "error"};

    public static void main(String[] args) throws IOException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.108.166");
        factory.setPort(5672);
        factory.setUsername("mqtt_cctv");
        factory.setPassword("cn.10020.cctv");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        //����һ���ǳ־õġ�Ψһ�ġ��Զ�ɾ���Ķ���
        String queueName = channel.queueDeclare().getQueue();
        String severity = getSeverity();
        //ָ��binding key
        channel.queueBind(queueName, EXCHANGE_NAME, severity);
        System.out.println(" [*] Waiting for " + severity + " logs. To exit press CTRL+C");

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        channel.basicConsume(queueName, true, queueingConsumer);

        while (true){
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }


    }


    /**
     * �������һ����־����
     *
     * @return
     */
    private static String getSeverity()
    {
        Random random = new Random();
        int ranVal = random.nextInt(3);
        System.out.println(ranVal);
        return SEVERITIES[ranVal];
    }
}
