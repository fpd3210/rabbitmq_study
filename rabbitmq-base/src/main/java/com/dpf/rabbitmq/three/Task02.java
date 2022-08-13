package com.dpf.rabbitmq.three;

import com.dpf.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * 手动应答举例
 *
 * @Author dpf
 * @Since 2021/7/11
 */
public class Task02 {

    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        // 声明队列
        boolean durable = true; // 设置队列的持久化
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            // MessageProperties.PERSISTENT_TEXT_PLAIN 消息持久化
            channel.basicPublish("", TASK_QUEUE_NAME, false, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息" + message);
        }

    }
}
