package com.dpf.rabbitmq.seven;

import com.dpf.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 优先级队列
 * @author Pikachues
 * Created 2022/8/13
 */
public class Producer {
    public static final String TASK_QUEUE_NAME = "priority_queue2";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        Map<String,Object> argument = new HashMap<>();
        argument.put("x-max-priority",10);

        // 声明队列
        boolean durable = true; // 设置队列的持久化
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, argument);

        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();

        for (int i = 1; i < 11; i++) {
           if(i==5){
               String message = "info"+i;
               channel.basicPublish("", TASK_QUEUE_NAME, false,
                       properties, message.getBytes(StandardCharsets.UTF_8));
           }else{
               String message = "info"+i;
               channel.basicPublish("", TASK_QUEUE_NAME, false,
                       null, message.getBytes(StandardCharsets.UTF_8));
           }
        }

    }
}
