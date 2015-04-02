package com.busap.amqp;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

/**
 * Created by liuyu on 2015/4/1.
 */
public class Work {

    private final static String QUEUE_NAME = "workqueue";

    public static void main(String[] args) throws IOException, InterruptedException {
        //���ֲ�ͬ���̵����
        int hashCode = Work.class.hashCode();

        //�������Ӻ��ŵ�
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.108.166");
        factory.setPort(5672);
        factory.setUsername("mqtt_cctv");
        factory.setPassword("cn.10020.cctv");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //��������
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(hashCode + "[*] waiting for message. To exit press ctrl + c ");

        //�������Ѷ���
        QueueingConsumer consumer = new QueueingConsumer(channel);
        //�����Ӧ�����
        boolean ack = false;
        //�ƶ����Ѷ���
        channel.basicConsume(QUEUE_NAME, ack, consumer);

        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(hashCode + "[x] Received '"+message+"'");
            doWork(message);
            System.out.println(hashCode + "[x] Done");
            //�ֶ�Ӧ���RabbitMQ������
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }

    /**
     * û���ʱ1s
     * @param task
     * @throws InterruptedException
     */
    private static void doWork(String task) throws InterruptedException {
        for(char ch: task.toCharArray()){
            if(ch == '.'){
                Thread.sleep(1000l);
            }
        }
    }
}
