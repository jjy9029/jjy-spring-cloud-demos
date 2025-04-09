package com.jjy.rabbit.service;

import com.rabbitmq.client.Channel;
import org.mockito.internal.stubbing.BaseStubbing;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitMqConsumer {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = "persisted-queue",
                    arguments = @Argument(name = "x-queue-mode", value = "lazy")
            ),
            exchange = @Exchange(name = "jjy.topic",type = ExchangeTypes.TOPIC),
            key = {"persisted"}
    ))
    public void receivePesistedMessage(String msg) {
        System.out.println("persisted-message: "+msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(
                    name = "non-persisted-queue",
                    arguments = @Argument(name = "x-queue-mode", value = "lazy")
            ),
            exchange = @Exchange(name = "jjy.topic",type = ExchangeTypes.TOPIC),
            key = {"non-persisted"}
    ))
    public void receiveNonPersistedMessage(String msg) {
        System.out.println("non-persisted-message: "+msg);
    }


    public void receiveOBJ(Message message){

    }






}
