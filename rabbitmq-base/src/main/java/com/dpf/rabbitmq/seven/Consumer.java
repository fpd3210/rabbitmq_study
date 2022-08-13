package com.dpf.rabbitmq.seven;

import com.dpf.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 优先级队列
 * @author Pikachues
 * Created 2022/8/13
 */
public class Consumer {
    public static final String TASK_QUEUE_NAME = "priority_queue2";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("接受到消息："+new String(message.getBody()));
        };

        CancelCallback cancelCallback = consumerTag->{
            System.out.println(consumerTag+"消费者取消消费接口的消费逻辑");
        };

        /**
         * 消费者消费消息
         * 1.消费哪个队列
         * 2.消费成功之后是否要自动应答 true 代表自动应答 false 手动应答
         * 3.接受到消息回调
         * 4.消费者未成功消费的回调
         */
        System.out.println("C2等待接受消息");
        channel.basicConsume(TASK_QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
