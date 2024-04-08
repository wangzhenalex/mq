package org.example.rabbitmqhello.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：消费者
 * @version: 1.0
 */
public class Consumer {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) {
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
            // 声明要消费的队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println(" [*] Waiting for messages. To exit press Ctrl+C");
            // 创建队列消费者
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("消息消费完毕 [x] Received '" + message + "'");
            };
            // 消费消息
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
