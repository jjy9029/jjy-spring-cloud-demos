package com.jjy.mq.service;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RocketMQMessageListener(topic = "test-topic",consumerGroup = "consumer-group")
public class RocketMqConsumer implements RocketMQListener<Map<String, String>> {
    @Override
    public void onMessage(Map<String, String> map) {
        System.out.println(map);
    }
}
