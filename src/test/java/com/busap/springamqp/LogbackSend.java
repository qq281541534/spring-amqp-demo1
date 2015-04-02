package com.busap.springamqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuyu on 2015/4/1.
 */
public class LogbackSend {

    private static Logger logger = LoggerFactory.getLogger(LogbackSend.class);

    public static void main(String[] args) {
        logger.debug("send message {}", "111");
    }
}
