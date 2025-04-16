package com.jjy.rabbit;

import com.jjy.rabbit.service.RabbitMqProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RabbitMqTest {
    @Autowired
    RabbitMqProducer rabbitMqProducer;
    @Test
    void  testMessage(){
       rabbitMqProducer.sendMessage();
    }

    @Test
    void  testDelayMessage(){
        rabbitMqProducer.sendMessageDelay();
    }
}
