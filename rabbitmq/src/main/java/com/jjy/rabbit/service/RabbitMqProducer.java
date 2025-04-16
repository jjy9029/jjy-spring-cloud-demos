package com.jjy.rabbit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



@Service
@Slf4j
public class RabbitMqProducer {
    @Autowired
    RabbitMqHelper rabbitMqHelper;
    public void sendMessage(){
        Map<String,Object> msg = new HashMap<>();
        msg.put("name","jjy");
        msg.put("age",30);
        msg.put("socore",100.0);
        msg.put("time", LocalDateTime.now());
        Map<String,Object> headers = new HashMap<>();
        headers.put("timestamp", LocalDateTime.now());
        rabbitMqHelper.sendMessage(
                "jjy.topic",
                "msg",
                msg,
                headers
        );
        System.out.println("send message success");
    }


    public void sendMessageDelay(){
        Map<String,Object> msg = new HashMap<>();
        msg.put("name","jjy");
        msg.put("age",30);
        msg.put("socore",100.0);
        Map<String,Object> headers = new HashMap<>();
        headers.put("timestamp", LocalDateTime.now());
        rabbitMqHelper.sendMessageWithDelay(
                "delay-exchange",
                "delay",
                msg,
                headers,
                10*1000
        );
    }
}
