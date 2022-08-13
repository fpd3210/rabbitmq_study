package com.dpf.rabbitmq.six.ttl;

import com.dpf.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author Pikachues
 * Created 2022/8/11
 */
public class Consumer2 {
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        String queueName = "dead-queue";
        channel.queueDeclare(queueName,false,false,true,null);
        // 绑定队列名称 交换机 跟路由key
        channel.queueBind(queueName,DEAD_EXCHANGE,"lisi");

        System.out.println("等待接收死信队列消息........... ");
        DeliverCallback deliverCallback =  (consumerTag, delivery)->{
            System.out.println("Consumer02 接收死信队列的消息"+new String(delivery.getBody(),"UTF-8"));
        };

        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});

    }
}
