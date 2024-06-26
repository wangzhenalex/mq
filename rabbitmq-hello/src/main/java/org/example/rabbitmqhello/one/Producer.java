package org.example.rabbitmqhello.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ地址
        factory.setHost("localhost");
        //        设置用户名
        factory.setUsername("guest");
        //        设置密码
        factory.setPassword("guest");
        //        创建一个新的连接
        try (Connection connection = factory.newConnection();
             // 创建一个通道
             Channel channel = connection.createChannel()) {
            // 声明一个队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 发送消息到队列中
            String message = "Hello World!";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送完毕 [x] Sent '" + message + "'");
        }
    }
}
