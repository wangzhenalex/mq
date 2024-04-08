package org.example.rabbitmqhello.two;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：hello
 * @version: 1.0
 */
public class Producer {
    //    队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtil.getChannel();
        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 10; i++) {
            // 发送消息到队列中
            String message = "Hello World!  ---" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        }

        System.out.println("消息发送完毕 [x] Sent");
    }
}
