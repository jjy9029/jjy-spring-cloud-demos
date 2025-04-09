package com.jjy.mq.service;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Service
public class RocketMqProvider {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    private final String topic = "test-topic";

    public void sendSyncMessage(String message){
        Map<String,String> map =  new HashMap<>();
        map.put("key",message);
        rocketMQTemplate.syncSend(topic, MessageBuilder.withPayload(map).build(),10000,delayMap.get("10s"));
        System.out.printf("同步发送结果: %s", message);
        rocketMQTemplate.syncSendOrderly("","","");
    }

    // 2.异步发送消息
    public void sendAsyncMessage(String message){
        rocketMQTemplate.asyncSend(topic, MessageBuilder.withPayload(message).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.printf("异步发送成功: %s", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.printf("异步发送失败: %s",throwable.getMessage());
            }
        },10000,delayMap.get("10s"));
    }

    // 3.单向发送消息
    public void sendOneWayMessage(String message){
        rocketMQTemplate.sendOneWay(topic, MessageBuilder.withPayload(message).build());
        System.out.println("单向消息发送成功");
    }

    private Map<String,Integer> delayMap = Map.ofEntries(
            Map.entry("1s", 1),
            Map.entry("5s", 2),
            Map.entry("10s", 3),
            Map.entry("30s", 4),
            Map.entry("1m", 5),
            Map.entry("2m", 6),
            Map.entry("3m", 7),
            Map.entry("4m", 8),
            Map.entry("5m", 9),
            Map.entry("6m", 10),
            Map.entry("7m", 11),
            Map.entry("8m", 12),
            Map.entry("9m", 13),
            Map.entry("10m", 14),
            Map.entry("20m", 15),
            Map.entry("30m", 16),
            Map.entry("1h", 17),
            Map.entry("2h", 18)
    );
}
