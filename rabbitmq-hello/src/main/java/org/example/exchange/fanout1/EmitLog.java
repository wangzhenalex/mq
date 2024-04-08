package org.example.exchange.fanout1;

import com.rabbitmq.client.Channel;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：日志发送
 * @version: 1.0
 */
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        try (Channel channel = RabbitmqUtil.getChannel()) {
            /**
             * 声明一个交换机
             * 1、交换机名称
             * 2、交换机类型
             */
            // fanout 广播类型
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            Scanner sc = new Scanner(System.in);
            System.out.println("请输入信息");

            while (sc.hasNext()) {
                String message = sc.nextLine();
                // 发送消息到交换机
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
                System.out.println("消息发送完毕 [x] Sent '" + message + "'");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
