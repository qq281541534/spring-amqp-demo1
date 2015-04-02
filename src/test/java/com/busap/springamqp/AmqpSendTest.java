package com.busap.springamqp;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuyu on 2015/3/31.
 */
public class AmqpSendTest {

    public static void main(String[] args){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-context.xml");
        MessageQueueManager manager = applicationContext.getBean(MessageQueueManager.class);
        manager.sendMessage("hello message", "cctv.sync");


    }
}
