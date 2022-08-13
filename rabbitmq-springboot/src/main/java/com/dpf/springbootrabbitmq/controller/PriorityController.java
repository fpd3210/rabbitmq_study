package com.dpf.springbootrabbitmq.controller;

import com.dpf.springbootrabbitmq.config.PriorityQueueConfig;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pikachues
 * Created 2022/8/13
 */
@RestController
public class PriorityController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/priority/{message}")
    public void send(@PathVariable("message") String message) {
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                rabbitTemplate.convertAndSend(PriorityQueueConfig.PRIORITY_EXCHANGE_NAME,
                        PriorityQueueConfig.PRIORITY_ROUTING_KEY_NAME, message + i, msg -> {
                            // 设置优先级
                            msg.getMessageProperties().setPriority(5);
                            return msg;
                        });
            } else {
                rabbitTemplate.convertAndSend(PriorityQueueConfig.PRIORITY_EXCHANGE_NAME,
                        PriorityQueueConfig.PRIORITY_ROUTING_KEY_NAME, message + i, msg -> msg);
            }
        }
    }
}
