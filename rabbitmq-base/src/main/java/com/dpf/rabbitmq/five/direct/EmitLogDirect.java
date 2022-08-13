package com.dpf.rabbitmq.five.direct;

import com.dpf.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pikachues
 * Created 2022/8/10
 */
public class EmitLogDirect {

    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception{
        Channel channel = RabbitMqUtils.getChannel();
        // 信道与交换机进行绑定
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Map<String,String> bindingKeyMap = new HashMap<String, String>(){{
            put("info","普通info信息");
            put("warning","警告warning信息");
            put("error","错误error信息");
            put("debug","调试debug信息");
        }};

        for (Map.Entry<String, String> bindingKeyEntity : bindingKeyMap.entrySet()) {
            String bindingKey = bindingKeyEntity.getKey();
            String message = bindingKeyEntity.getValue();
            channel.basicPublish(EXCHANGE_NAME,bindingKey,null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发出消息:" + message);
        }
    }
}
