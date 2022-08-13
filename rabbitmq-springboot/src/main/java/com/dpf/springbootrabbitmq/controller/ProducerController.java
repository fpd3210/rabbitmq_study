package com.dpf.springbootrabbitmq.controller;

import com.dpf.springbootrabbitmq.config.ConfirmConfig;
import com.dpf.springbootrabbitmq.config.MyCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * @author Pikachues
 * Created 2022/8/12
 */
@Slf4j
@RequestMapping("/confirm")
@RestController
public class ProducerController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MyCallBack myCallBack;

    /**
     * 依赖注入 rabbitTemplate 之后再设置它的回调对象
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myCallBack);

        rabbitTemplate.setMandatory(true);
        //设置回退消息交给谁处理
        rabbitTemplate.setReturnsCallback(myCallBack);
    }
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.ROUTING_KEY+"1",message,correlationData);
        log.info("发送消息内容:{}",message);
    }

    @GetMapping("/sendMessage2/{message}")
    public void sendMessage2(@PathVariable String message){
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
                ConfirmConfig.ROUTING_KEY+"1",message);
        log.info("发送消息内容:{}",message);
    }
}
