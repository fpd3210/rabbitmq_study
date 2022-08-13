package com.dpf.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pikachues
 * Created 2022/8/11
 */
@Configuration
public class TtlQueueConfig {

    public static final String X_EXCHANGE = "X";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";

    public static final String Y_DEAD_EXCHANGE = "Y";
    public static final String DEAD_LETTER_QUEUE = "QD";

    /**
     * 声明名为xExchange的交换机
     * @return
     */
    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }

    /**
     * 声明名为yExchange的交换机
     * @return
     */
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_EXCHANGE);
    }

    /**
     * 声明队列 A ttl 为 10s 并绑定到对应的死信交换机
     * @return
     */
    @Bean("queueA")
    public Queue queueA(){
        HashMap<String, Object> args = new HashMap<>();
        // 声明当前队列绑定的死信队列交换机
        args.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        // 声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key","YD");
        // 声明队列的TTL
        args.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
    }

    /**
     * 声明队列A绑定xExchange交换机
     * @param queueA
     * @param xExchange
     * @return
     */
    @Bean
    public Binding queueaBindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    /**
     * 声明队列 B ttl 为 40s 并绑定到对应的死信交换机
     * @return
     */
    @Bean("queueB")
    public Queue queueB(){
        HashMap<String, Object> args = new HashMap<>();
        // 声明当前队列绑定的死信队列交换机
        args.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        // 声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key","YD");
        // 声明队列的TTL
        args.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
    }

    /**
     * 声明队列B绑定xExchange交换机
     * @param queueB
     * @param xExchange
     * @return
     */
    @Bean
    public Binding queuebBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    /**
     * 声明队列QC
     * @return
     */
    @Bean("queueC")
    public Queue queueC(){
        Map<String, Object> args = new HashMap<>();
        // 声明当前队列绑定的死信队列交换机
        args.put("x-dead-letter-exchange",Y_DEAD_EXCHANGE);
        // 声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key","YD");

        return QueueBuilder.durable(QUEUE_C).withArguments(args).build();
    }

    /**
     * 声明队列C绑定xExchange交换机
     * @return
     */
    @Bean
    public Binding queuecBindingX(@Qualifier("queueC") Queue queueC,
                                  @Qualifier("xExchange") DirectExchange xExchange){
        return BindingBuilder.bind(queueC).to(xExchange).with("XC");
    }


    /**
     * 声明死信队列QD
     * @return
     */
    @Bean("queueD")
    public Queue queueD(){
        return new Queue(DEAD_LETTER_QUEUE);
    }

    /**
     * 声明队列QD绑定yExchange交换机
     * @param queueD
     * @param yExchange
     * @return
     */
    @Bean
    public Binding deadLetterBindingQAD(@Qualifier("queueD") Queue queueD,
                                        @Qualifier("yExchange") DirectExchange yExchange){
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }
}
