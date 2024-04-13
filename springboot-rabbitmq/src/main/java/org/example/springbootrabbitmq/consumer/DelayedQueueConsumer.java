package org.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootrabbitmq.config.DelayedQueueConfig;
import org.example.springbootrabbitmq.config.TTLQueueConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : zhen wang
 * @date : 2024/4/12 21:31
 * @Version: 1.0
 * @Desc : 队列TTL的消费者
 */
@Component
@Slf4j
public class DelayedQueueConsumer {
    /**
     * 进行接收消息
     */
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE)
    public void receiveD(Message msg, Channel channel) {
        log.info("当前时间：{},收到延迟队列的消息：{}", LocalDateTime.now(), new String(msg.getBody()));
    }
}
