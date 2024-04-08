package org.example.deadmsg;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        //不建议在这里设置，建议在生产设置
        //        arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置队列的长度,超过长度后会进入私信队列
        //        arguments.put("x-max-length", 5);

        //--------------------------------
        //死信队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定交换机和队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("等待接收消息.....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String info = new String(message.getBody());
            System.out.println("Consumer01 接收到的消息：" +info);
            if (Objects.equals(info, "info5")) {
                System.out.println("Consumer01 拒绝接收消息：" + info);
                //拒绝接收消息
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            }else {
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };

        //开启手动应答
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
            System.out.println("接收消息被中断");
        });
    }

}
