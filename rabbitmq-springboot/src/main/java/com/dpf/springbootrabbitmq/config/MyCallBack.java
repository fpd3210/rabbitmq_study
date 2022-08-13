package com.dpf.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * Rabbitmq回调
 * RabbitTemplate.ConfirmCallback 交换机回调
 * RabbitTemplate.ReturnsCallback 消息回退
 *
 * @author Pikachues
 * Created 2022/8/12
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {
    /**
     * 交换机不管是否收到消息的一个回调方法
     * CorrelationData 消息相关数据
     * ack 交换机是否收到消息
     * cause 交换机未收到消息原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到 id 为:{}的消息", id);
        } else {
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}", id, cause);
        }
    }

    /**
     * 无法被路由的消息回退回调
     * @param returned
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {

        log.error("消息 {}, 被交换机{}退回 ，退回原因:{},路由key:{}",new
                String(returned.getMessage().getBody()),returned.getExchange(),returned.getReplyText(),returned.getRoutingKey());
    }
}
