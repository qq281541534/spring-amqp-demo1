package com.busap.amqp.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * �㲥ģʽ
 * ��Ϣ���Ѷˣ�������һ���ǳ־ã�Ψһ�ģ��Զ�ɾ���Ķ��У���ת�����󶨣����ܵ���Ϣ����Ϣд�뱾����־
 * Created by liuyu on 2015/4/1.
 */
public class Cousumer {


    private final static String EXCHANGE_NAME = "ex_log";

    public static void main(String[] args) throws IOException, InterruptedException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.108.166");
        factory.setPort(5672);
        factory.setUsername("mqtt_cctv");
        factory.setPassword("cn.10020.cctv");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //����һ���ǳ־õġ�Ψһ�ġ��Զ�ɾ���Ķ���
        String queueName = channel.queueDeclare().getQueue();
        //Ϊת�����󶨶���
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //�ƶ������ߣ��ڶ�������Ϊ�Ƿ��Զ�Ӧ�����������ֶ�Ӧ��
        channel.basicConsume(queueName, true, queueingConsumer);

        while(true) {
            //��ȡ��������
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String message = new String(delivery.getBody());
            print2File(message);
        }


    }

    /**
     * д�뱾���ļ�
     * @param message
     */
    private static void print2File(String message){
        try {

            String dir = Cousumer.class.getClassLoader().getResource("").getPath();
            String logFileName = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            File file = new File(dir, logFileName+".txt");
            System.out.println("message : " + dir + ":" + message);
            FileOutputStream out = new FileOutputStream(file);
            out.write((message + "/r/n").getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
