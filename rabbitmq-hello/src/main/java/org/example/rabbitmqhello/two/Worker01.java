package org.example.rabbitmqhello.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：工作线程 1
 * @version: 1.0
 */
public class Worker01 {
    //    队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();

        CancelCallback callback = consumerTag -> {
            System.out.println("取消消费");
        };

        System.out.println("消费者 " + args[0] + " 准备接收消息。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("消费者收到消息：" + new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(QUEUE_NAME, false, deliverCallback, callback);
    }
}
