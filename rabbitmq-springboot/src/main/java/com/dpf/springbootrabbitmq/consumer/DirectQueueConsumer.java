package com.dpf.springbootrabbitmq.consumer;

import com.dpf.springbootrabbitmq.config.DirectQueueConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Pikachues
 * Created 2022/8/12
 */
@Component
public class DirectQueueConsumer {

    @RabbitListener(queues = DirectQueueConfig.QUEUE_Q1_NAME)
    public void received(Map message){
        System.out.println(message.toString());
    }
}
