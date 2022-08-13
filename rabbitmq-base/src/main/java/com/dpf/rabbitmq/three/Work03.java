package com.dpf.rabbitmq.three;

import com.dpf.rabbitmq.utils.RabbitMqUtils;
import com.dpf.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 手动应答举例
 * @Author dpf
 * @Since 2021/7/11
 *
 * 消息在手动应答时是不丢失的，会自动放回队列中重新消费
 */
public class Work03 {
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("C1等待消息接受时间较短");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            SleepUtils.sleep(1);

            System.out.println("接受到消息"+new String(message.getBody(),"UTF-8"));

            /**
             * 手动应答
             * 1.消息的唯一标记
             * 2.是否批量应答，批量应答可能会导致消息丢失
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };

        // 设置不公平分发(默认轮询分发)，不公平分发(能者多劳)
        channel.basicQos(2);

        // 采用手动应答  false手动应答，true自动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,(consumerTag -> {
            System.out.println("消费者取消消费接口的回调逻辑");
        }));
    }
}
