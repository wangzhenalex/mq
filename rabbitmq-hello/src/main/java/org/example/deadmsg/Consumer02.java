package org.example.deadmsg;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：死信队列-消费者 1
 * @version: 1.0
 */
public class Consumer02 {

    //死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();

        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        System.out.println("等待接收消息.....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02 接收到的消息：" + new String(message.getBody()));
        };

        channel.basicConsume(DEAD_QUEUE, true,deliverCallback, consumerTag -> {
            System.out.println("接收消息被中断");
        });
    }

}
