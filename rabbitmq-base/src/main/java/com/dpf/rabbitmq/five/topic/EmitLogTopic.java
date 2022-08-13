package com.dpf.rabbitmq.five.topic;

import com.dpf.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pikachues
 * Created 2022/8/10
 */
public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 信道与交换机进行绑定
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        /*
        quick.orange.rabbit 被队列 Q1Q2 接收到
        lazy.orange.elephant 被队列 Q1Q2 接收到
        quick.orange.fox 被队列 Q1 接收到
        lazy.brown.fox 被队列 Q2 接收到
        lazy.pink.rabbit 虽然满足两个绑定但只被队列 Q2 接收一次
        quick.brown.fox 不匹配任何绑定不会被任何队列接收到会被丢弃
        quick.orange.male.rabbit 是四个单词不匹配任何绑定会被丢弃
        lazy.orange.male.rabbit 是四个单词但匹配 Q2
         */
        Map<String,String> bindingKeyMap = new HashMap<String, String>(){{
            put("quick.orange.rabbit","被队列 Q1Q2 接收到");
            put("lazy.orange.elephant","被队列 Q1Q2 接收到");
            put("quick.orange.fox","被队列 Q1 接收到");
            put("lazy.brown.fox","被队列 Q2 接收到");
            put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
            put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
            put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
            put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");
        }};

        for (Map.Entry<String, String> bindingKeyEntity : bindingKeyMap.entrySet()) {
            String bindingKey = bindingKeyEntity.getKey();
            String message = bindingKeyEntity.getValue();
            channel.basicPublish(EXCHANGE_NAME,bindingKey,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息:" + message);
        }
    }
}
