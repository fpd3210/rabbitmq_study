package com.dpf.springbootrabbitmq.consumer;

import com.dpf.springbootrabbitmq.config.PriorityQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 优先级队列消费者
 * 演示需要分两个端，生成者发送玩消息，再启动生产者
 * @author Pikachues
 * Created 2022/8/13
 */
@Slf4j
@Component
public class PriorityConsumer {

    @RabbitListener(queues = PriorityQueueConfig.PRIORITY_QUEUE_NAME)
    public void receiver(Message message){
        String msg = new String(message.getBody());
        log.info("接收到优先级消息：{}",msg);
    }
}
