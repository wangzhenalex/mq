package org.example.rabbitmqhello.three;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.example.rabbitmqhello.utils.RabbitmqUtil;
import org.example.rabbitmqhello.utils.SleepUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：消息在手动英大是不丢失、放回队列重新消费
 * @version: 1.0
 */
public class Worker03 {
    //    任务队列名称
    private final static String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();


        //预取值
        int preFetchCount = 5;


        //不公平分发
        channel.basicQos(preFetchCount);


        System.out.println("消费者 3 准备接收消息。时间较短 1 秒完成任务");

        CancelCallback callback = consumerTag -> {
            System.out.println("取消消费");
        };

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            SleepUtil.sleep(1);
            System.out.println("消费者收到消息：" + new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, callback);
    }
}
