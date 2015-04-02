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
 * 广播模式
 * 消息消费端，创建了一个非持久，唯一的，自动删除的队列，与转发器绑定，接受到消息后将消息写入本地日志
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
        //创建一个非持久的、唯一的、自动删除的队列
        String queueName = channel.queueDeclare().getQueue();
        //为转发器绑定队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //制定接受者，第二个参数为是否自动应答，设置无需手动应答
        channel.basicConsume(queueName, true, queueingConsumer);

        while(true) {
            //获取交付对象
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String message = new String(delivery.getBody());
            print2File(message);
        }


    }

    /**
     * 写入本地文件
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
