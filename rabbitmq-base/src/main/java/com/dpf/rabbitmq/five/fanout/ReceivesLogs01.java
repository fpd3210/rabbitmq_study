package com.dpf.rabbitmq.five.fanout;

import com.dpf.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

/**
 * @Author dpf
 * @Since 2021/8/9
 * 消费者
 */
public class ReceivesLogs01 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception{

        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        /**
         * 生成一个临时队列（名字随机），当消费者断开和该队列的连接时，队列自动删除
         */
        String queueName = channel.queueDeclare().getQueue();

        // 把临时队列和exchange进行绑定,routingKey也称为BindingKey
        channel.queueBind(queueName,EXCHANGE_NAME,"");

        System.out.println("ReceivesLogs01等待接受到的消息...");
        DeliverCallback deliverCallback = ( consumerTag,  message)->{
            System.out.println("接受到消息"+new String(message.getBody(),"UTF-8"));
        };


        channel.basicConsume(queueName,deliverCallback,(consumerTag)->{ });


    }
}
