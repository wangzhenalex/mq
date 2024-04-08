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
public class Consumer01 {
    //普通交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    //死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";

    //普通队列
    public static final String NORMAL_QUEUE = "normal_queue";

    //死信队列
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        //声明一个普通交换机，类型是direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明一个普通队列
        Map<String, Object> arguments = new HashMap<>();
        //正常队列设置私信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-routing-key", "lisi");
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        //--------------------------------
        //死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定交换机和队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息.....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer01 接收到的消息：" + new String(message.getBody()));
        };

        channel.basicConsume(DEAD_QUEUE, true,deliverCallback, consumerTag -> {
            System.out.println("接收消息被中断");
        });
    }

}
