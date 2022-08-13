package com.dpf.rabbitmq.four;

import com.dpf.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author dpf
 * @Since 2021/7/26
 * 发布确认模式
 *  1.单个确认发布
 *  2.批量确认发布
 *  3.异步确认发布
 */
public class ConfirmMessage {

    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        ConfirmMessage.publicMessageIndividually();

        ConfirmMessage.publicMessageBatch();

        ConfirmMessage.publicMessageAsync();
    }

    /**
     * 单个发布确认
     */
    public static void publicMessageIndividually() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 定义队列
        channel.queueDeclare(queueName,false,false,false,null);
        // 开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            // 服务端返回false或超时时间内，生产者可以消息重发
            boolean flag = channel.waitForConfirms();
            if(flag){
                //System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"条单独确认消息，耗时"+(end-begin)+"ms");
    }

    /**
     * 批量确认模式
     * @throws Exception
     */
    public static void publicMessageBatch() throws Exception{

        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 定义队列
        channel.queueDeclare(queueName,false,false,false,null);
        // 开启发布确认
        channel.confirmSelect();
        long begin = System.currentTimeMillis();
        // 批量确认消息大小
        int batchSize = 100;
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //达到100条时，确认一次
            if(i%batchSize==0){
                // 服务端返回false或超时时间内，生产者可以消息重发
                channel.waitForConfirms();
                //System.out.println("消息发送成功");
            }
        }
        channel.waitForConfirms();
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"条批量确认消息，耗时"+(end-begin)+"ms");
    }


    /**
     * 异步确认模式
     * @throws Exception
     */
    public static void publicMessageAsync() throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        // 定义队列
        channel.queueDeclare(queueName,false,false,false,null);
        // 开启发布确认
        channel.confirmSelect();
        /**
         * 线程安全的一个Hash表
         */
        ConcurrentSkipListMap<Long,String> outstandingConfirm = new ConcurrentSkipListMap<>();

        /**
         * 确认收到消息回调
         * deliveryTag 消息的一个标识
         * multiple 是否批量确认
         */
        ConfirmCallback ackCallback = ( deliveryTag,  multiple)->{
            if (multiple){
                // 返回未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirm.headMap(deliveryTag,true);
                // 删除确认的消息
                confirmed.clear();
            }else {
                outstandingConfirm.remove(deliveryTag);
            }
        };

        /**
         * 未收到消息回调
         */
        ConfirmCallback nackCallback = ( deliveryTag,  multiple)->{
            String message = outstandingConfirm.get(deliveryTag);
            System.out.println("发布消息："+message+"未被确认，序列号"+deliveryTag);
        };

        /**
         * 添加异步监听器
         * ackCallback 发送成功监听器
         * nackCallback 发送失败监听器
         */
        channel.addConfirmListener(ackCallback,nackCallback);


        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i+"";
            /**
             *  channel.getNextPublishSeqNo() 获取下一消息序列号
             *  使未确认消息和序列号进行关联
             */
            outstandingConfirm.put(channel.getNextPublishSeqNo(),message);
            channel.basicPublish("",queueName,null,message.getBytes());
        }
        channel.waitForConfirms();
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT+"条异步确认消息，耗时"+(end-begin)+"ms");
    }
}
