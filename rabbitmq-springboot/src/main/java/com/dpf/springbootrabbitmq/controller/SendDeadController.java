package com.dpf.springbootrabbitmq.controller;

import com.dpf.springbootrabbitmq.config.DelayedQueueConfig;
import com.dpf.springbootrabbitmq.config.DirectQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Pikachues
 * Created 2022/8/11
 */
@Slf4j
@RequestMapping("ttl")
@RestController
public class SendDeadController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试死信队列，队列设置过期时间
     * 缺点：每添加一个过期时间都要创建一个队列
     *
     * @param message
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给两个 TTL 队列:{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自 ttl 为 10S 的队列: " + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自 ttl 为 40S 的队列: " + message);
    }

    /**
     * 测试死信队列，客户端发送时设置过期时间
     * 存在问题：按顺序消费,先过期的队列不一定先被消费
     *  http://localhost:8080/ttl/sendExpirationMsg/你好 1/20000
     *  http://localhost:8080/ttl/sendExpirationMsg/你好 1/2000
     * @param message
     * @param ttlTime
     */
    @GetMapping("/sendMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable("ttlTime") String ttlTime) {
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}", new Date(),ttlTime, message);
        rabbitTemplate.convertAndSend("X","XC",message,msg->{
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    /**
     * 带rabbitmq_delayed_message_exchange插件延时队列测试
     * @param message
     * @param delayTime
     */
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message,@PathVariable Integer delayTime) {
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY, message,
                correlationData -> {
                    correlationData.getMessageProperties().setDelay(delayTime);
                    return correlationData;
                });
    }


    @GetMapping("sendMsg2/{message}")
    public void sendMsg2(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给队列:{}", new Date(), message);

        String messageId = String.valueOf(UUID.randomUUID());
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("message", message);
        map.put("createTime", createTime);

        rabbitTemplate.convertAndSend(DirectQueueConfig.EXCHANGE_Q1_NAME, DirectQueueConfig.ROUTING_Q1_KEY, map);
    }
}
