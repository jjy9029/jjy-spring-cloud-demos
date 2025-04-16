package com.jjy.rabbit.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;
import java.util.Map;

@Component
@Slf4j
public class RabbitMqHelper {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    // 发送普通消息
    public void sendMessage(String exchangeName, String routingKey, Object msg, Map<String,Object> headers){
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg,message ->{
           if (headers!= null &&!headers.isEmpty()){
               for (Map.Entry<String, Object> entry : headers.entrySet()){
                   message.getMessageProperties().setHeader(entry.getKey(),entry.getValue());
               }
           }
           return message;
        });
    }

    // 发送延时消息
    // 需要rabbitmq安装延时插件
    // 并且exchageName指向的交换机的类型要是延时交换机（@rabbitmqListener中的exchange的delayed属性设置为true）
    public void sendMessageWithDelay(String exchangeName, String routingKey, Object msg, Map<String,Object> headers,Integer delayTime){
        rabbitTemplate.convertAndSend(exchangeName,routingKey,msg,message -> {
            if (headers!= null &&!headers.isEmpty()){
                for (Map.Entry<String, Object> entry : headers.entrySet()){
                    message.getMessageProperties().setHeader(entry.getKey(),entry.getValue());
                }
            }
            message.getMessageProperties().setDelay(delayTime);
            return message;
        });
        System.out.println(LocalDateTime.now());
    }



    public void sendMessageWithCallBack(String exchangeName, String routingKey, Object msg,Map<String,Object> headers){
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
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, message -> {
            if (headers!= null &&!headers.isEmpty()) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    message.getMessageProperties().setHeader(entry.getKey(), entry.getValue());
                }
            }
            return message;
        },cd);
    }


    public void sendDelayMessageWithCallBack(String exchangeName, String routingKey, Object msg, Map<String,Object> headers, Integer delayTime){
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
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, message -> {
            if (headers!= null &&!headers.isEmpty()) {
                for (Map.Entry<String, Object> entry : headers.entrySet()) {
                    message.getMessageProperties().setHeader(entry.getKey(), entry.getValue());
                }
            }
            message.getMessageProperties().setDelay(delayTime);
            return message;
        },cd);
    }

}
