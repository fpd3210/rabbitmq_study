package com.dpf.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Pikachues
 * Created 2022/8/11
 */
@Configuration
public class DirectQueueConfig {
    // 队列名称
    public static final String QUEUE_Q1_NAME = "testQ1";
    // 交换机名称
    public static final String EXCHANGE_Q1_NAME = "testExchangeQ1";
    // 路由名称
    public static final String ROUTING_Q1_KEY = "routingKeyQ1";

    /**
     * 声明队列
     * @return
     */
    @Bean("testQ1")
    public Queue testQ1(){
        return QueueBuilder.durable(QUEUE_Q1_NAME).build();
    }

    /**
     * 声明交换机
     * @return
     */
    @Bean("exchangeQ1")
    public DirectExchange exchangeQ1(){
       return new DirectExchange(EXCHANGE_Q1_NAME);
    }

    /**
     * 用routingKey绑定交换机与队列
     * @return
     */
    @Bean
    public Binding bindingDirect(){
       return BindingBuilder.bind(testQ1()).to(exchangeQ1()).with(ROUTING_Q1_KEY);
    }
}
