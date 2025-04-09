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
import java.util.Map;
import java.util.Objects;



@Service
@Slf4j
public class RabbitMqProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendPersistedMessage(){
        String exchageName = "jjy.topic";
        String msg = "Hello World";
        for (int i = 0; i <1000 ; i++) {
            rabbitTemplate.convertAndSend(exchageName,"persisted",msg+i,message -> {
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                return message;
            });
        }
    }





    public void sendNonPersistedMessage(){
        String exchageName = "jjy.topic";
        String msg = "Hello World";
        for (int i = 0; i <100 ; i++) {
            rabbitTemplate.convertAndSend(exchageName,"non-persisted",msg+i,message -> {
                message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
                return message;
            });

        }
    }


    // 此函数用于确认消息是否到达交换机
    public void sendWithConfirmCallback(){

        CorrelationData cd = new CorrelationData();
        // 2.给Future添加ConfirmCallback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发s
                log.error("send message fail", ex);
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
                if(result.isAck()){ // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                    System.out.println("ack");
                }else{ // result.getReason()，String类型，返回nack时的异常描述
                    System.out.println("nack");
                }
            }
        });

        rabbitTemplate.convertAndSend("", "", "", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
              return message;
            }
        }, cd);


    }

}
