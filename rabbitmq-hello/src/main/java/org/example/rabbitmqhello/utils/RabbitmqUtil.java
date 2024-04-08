package org.example.rabbitmqhello.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：工具类
 * @version: 1.0
 */
public class RabbitmqUtil {
    /**
     * 获取通道
     *
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Channel getChannel() throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ地址
        factory.setHost("localhost");
        //        设置用户名
        factory.setUsername("guest");
        //        设置密码
        factory.setPassword("guest");
        return factory.newConnection().createChannel();
    }
}
