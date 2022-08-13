package com.dpf.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 优先级队列配置
 * @author Pikachues
 * Created 2022/8/13
 */
@Configuration
public class PriorityQueueConfig {
    public static final String PRIORITY_EXCHANGE_NAME = "priority_exchange";
    public static final String PRIORITY_QUEUE_NAME = "priority_queue";
    public static final String PRIORITY_ROUTING_KEY_NAME = "priority_routing_key1";

    @Bean
    public Queue priorityQueue(){
        return QueueBuilder
                .durable(PRIORITY_QUEUE_NAME)
                // 设置最大优先级即优先级范围(0~255)
                .maxPriority(10)
                .build();
    }

    @Bean
    public DirectExchange priorityExchange(){
        return new DirectExchange(PRIORITY_EXCHANGE_NAME);
    }

    @Bean
    public Binding bindingPriorityExchange(){
        return BindingBuilder.bind(priorityQueue()).to(priorityExchange()).with(PRIORITY_ROUTING_KEY_NAME);
    }

}
