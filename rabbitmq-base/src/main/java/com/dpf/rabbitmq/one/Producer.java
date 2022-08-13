package com.dpf.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author dpf
 * @Since 2021/6/30
 */
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");

        try {
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();

            /**
             * 生成一个队列
             * 1.队列名字
             * 2.队列里的消息是否持久化，默认存储在内存中
             * 3.该队列是否只供一个消费者进行消费 是否进行共享 true 可以多个消费者消费
             * 4.最后一个消费者端开启后，该队列是否自动删除
             */
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);

            String message = "hello world";

            /**
             * 发送一个消息
             * 1.发送消息用到哪个交换机
             * 2.路由的key是哪个
             * 3.其他的参数信息
             * 4.发送消息的消息体
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

            System.out.println("发送消息成功");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }
}
