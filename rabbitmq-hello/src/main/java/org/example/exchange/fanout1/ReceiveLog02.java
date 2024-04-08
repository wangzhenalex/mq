package org.example.exchange.fanout1;

import com.rabbitmq.client.Channel;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：接收
 * @version: 1.0
 */
public class ReceiveLog02 {

    private static final String EXCHANGE_NAME = "logs";


    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        //声明一个队列 临时队列,队列名称随机，非持久，独占，自动删除
        String queueName = channel.queueDeclare().getQueue();

        //绑定交换机和队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("等待接收消息，把接收到的消息打印在控制台上");

        //接收消息
        channel.basicConsume(queueName, true, (consumerTag, message) -> {
            System.out.println("ReceiveLog02 接收到的消息,并写入文件：" + new String(message.getBody()));
        }, consumerTag -> {
            System.out.println("接收消息被中断");
        });
    }
}
