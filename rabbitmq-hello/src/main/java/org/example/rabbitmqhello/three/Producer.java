package org.example.rabbitmqhello.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：生产者
 * @version: 1.0
 */
public class Producer {
    //    队列名称
    private final static String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitmqUtil.getChannel();
        // 声明一个队列
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);

        //从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            // 发送消息到队列中，设置消息持久化
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("消息发送完毕 [x] Sent '" + message + "'");
        }
    }
}
