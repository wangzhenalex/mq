package org.example.deadmsg;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.example.rabbitmqhello.utils.RabbitmqUtil;
import org.example.rabbitmqhello.utils.SleepUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：死信队列-生产者
 * @version: 1.0
 */
public class Producer {
    private static final String EXCHANGE_NAME = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();

        //死信消息 设置 TTL 时间
//        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i = 0; i < 10; i++) {
            String message = "info" + i;
//            SleepUtil.sleep(1);
            channel.basicPublish(EXCHANGE_NAME, "zhangsan", null, message.getBytes());
            System.out.println("生产者发送消息：" + message);
        }
    }
}
