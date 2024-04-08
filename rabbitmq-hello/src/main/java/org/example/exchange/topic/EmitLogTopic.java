package org.example.exchange.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.example.rabbitmqhello.utils.RabbitmqUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) {
        try (Channel channel = RabbitmqUtil.getChannel()) {

            /*
             * Q1-->绑定的是
             *           中间带orange的3个单词的信道(*.orange.*)
             * Q2-->绑定的是
             *           最后一个单词是rabbit的3个单词的信道(*.*.rabbit)
             *           第一个单词是lazy的多个单词的信道(lazy.#)
             **/
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            HashMap<String, String> bindingKeymap = new HashMap<>();
            bindingKeymap.put("quick.orange.rabbit", "被队列Q1Q2接收到");
            bindingKeymap.put("lazy.orange.elephant", "被队列Q1Q2接收到");
            bindingKeymap.put("quick.orange.fox", "被队列Q1接收到");
            bindingKeymap.put("lazy.brown.fox", "被队列Q2接收到");
            bindingKeymap.put("lazy.pink.rabbit", "虽然满足两个绑定，但只被队列Q2接收一次");
            bindingKeymap.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到，会被丢弃");
            bindingKeymap.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定，会被丢弃");
            bindingKeymap.put("lazy.orange.male.rabbit", "被Q2接收到");

            for (Map.Entry<String, String> bindingKeyEntry : bindingKeymap.entrySet()) {
                String routingKey = bindingKeyEntry.getKey();
                String message = bindingKeyEntry.getValue();
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
                System.out.println("生产者发出消息：" + message);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
