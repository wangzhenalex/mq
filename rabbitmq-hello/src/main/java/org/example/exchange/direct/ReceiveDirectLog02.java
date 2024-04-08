package org.example.exchange.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：
 * @version: 1.0
 */
public class ReceiveDirectLog02 {
    //交换机
    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String QUEUE_NAME = "disk";


    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //绑定交换机和队列
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");

        System.out.println("等待接收消息，把接收到的消息打印在控制台上");

        //接收消息
        channel.basicConsume(QUEUE_NAME, true, (consumerTag, message) -> {
            System.out.println("ReceiveDirectLog02 接收到的消息：" + new String(message.getBody()));
        }, consumerTag -> {
            System.out.println("接收消息被中断");
        });
    }
}
