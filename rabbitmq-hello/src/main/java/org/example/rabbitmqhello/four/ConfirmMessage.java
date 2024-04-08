package org.example.rabbitmqhello.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：发布确认模式 1、单个确认
 * 2、批量确认
 * 3、异步批量确认
 * @version: 1.0
 */
public class ConfirmMessage {
    //批量发消息的个数
    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
//        // 1、单个确认发送1000条消息，耗时：187ms
//        singleConfirm();
//
//        // 2、批量确认发送1000条消息，耗时：17ms
//        batchConfirm();

        // 3、异步批量确认发送1000条消息，耗时：17ms
        asyncBatchConfirm();

    }

    /**
     * 单个确认
     */
    public static void singleConfirm() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitmqUtil.getChannel();
        //队列名称
        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long start = System.currentTimeMillis();

        //发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //确认消息
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        //耗时
        System.out.println("单个确认发送" + MESSAGE_COUNT + "条消息，耗时：" + (end - start) + "ms");
    }

    public static void batchConfirm() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitmqUtil.getChannel();
        //队列名称
        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long start = System.currentTimeMillis();

        //批量确认的长度
        int batchSize = 100;

        //发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //达到批量确认的数量
            if (i % batchSize == 0) {
                //确认消息
                boolean flag = channel.waitForConfirms();
                if (flag) {
                    System.out.println("消息发送成功");
                }
            }
        }
        //结束时间
        long end = System.currentTimeMillis();
        //耗时
        System.out.println("批量确认发送" + MESSAGE_COUNT + "条消息，耗时：" + (end - start) + "ms");
    }

    /**
     * 异步批量确认
     */
    public static void asyncBatchConfirm() throws IOException, TimeoutException {
        Channel channel = RabbitmqUtil.getChannel();
        //队列名称
        String queueName = UUID.randomUUID().toString();
        //声明队列
        channel.queueDeclare(queueName, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        ConcurrentSkipListMap<Long,String> confirmSet = new ConcurrentSkipListMap<>();

        //开始时间
        long start = System.currentTimeMillis();

        //成功消息的监听器
        ConfirmCallback ackCallBack = (deliveryTag, multiple) -> {
            //删除已经确认的消息
            if (multiple) {
                ConcurrentNavigableMap<Long,String> confirmed = confirmSet.headMap(deliveryTag, true);
                confirmed.clear();
            } else {
                confirmSet.remove(deliveryTag);
            }
            System.out.println("消息发送成功:" + deliveryTag);
        };

        //失败消息的监听器
        ConfirmCallback nackCallBack = (deliveryTag, multiple) -> {
            //添加 multiple 为 true 的判断
            if (multiple) {
                //todo
            }
            System.out.println("未确认消息:" + deliveryTag);
            //未确认的消息重新
            String message = confirmSet.get(deliveryTag);
            //打印未确认的消息
            System.out.println("未确认的消息:" + message);
        };

        channel.addConfirmListener(ackCallBack, nackCallBack);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //记录每一条消息的deliveryTag
            confirmSet.put(channel.getNextPublishSeqNo(),message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("异步批量确认发送" + MESSAGE_COUNT + "条消息，耗时：" + (end - start) + "ms");

    }
}
