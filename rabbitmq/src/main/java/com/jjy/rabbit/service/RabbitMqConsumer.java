package com.jjy.rabbit.service;

import com.rabbitmq.client.Channel;
import org.mockito.internal.stubbing.BaseStubbing;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class RabbitMqConsumer {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "jjy-queue",durable = "true",arguments = @Argument(name ="x-queue-mode",value = "lazy")),
            exchange = @Exchange(name = "jjy.topic",type = ExchangeTypes.TOPIC,durable = "true"),
            key = {"msg"}
    ))
    public void receiveMessage(@Payload Map<String,Object> body, @Headers Map<String,Object> headers,Message message){
        System.out.println(body);
        System.out.println(headers);
        System.out.println(message.getMessageProperties().getMessageId());
    }



    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay-queue",durable = "true",arguments = @Argument(name ="x-queue-mode",value = "lazy")),
            exchange = @Exchange(name = "delay-exchange",type = ExchangeTypes.TOPIC,durable = "true",delayed = "true"),
            key = {"delay"}
    ))
    public void recieveDelaysMessage(@Payload Map<String,Object> body, @Headers Map<String,Object> headers,Message message){
        System.out.println(LocalDateTime.now());
        System.out.println(body);
        System.out.println(headers);
        System.out.println(message.getMessageProperties().getMessageId());
    }




}
