package org.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootrabbitmq.config.ConfirmConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : zhen wang
 * @date : 2024/4/13 10:32
 * @Version: 1.0
 * @Desc : 高级确认接收消息
 */
@Component
@Slf4j
public class Consumer {

    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMsg(Message message, Channel channel) {
        //打印消息
        log.info("接收到消息的队列 confirm.queue：" + new String(message.getBody()));
    }
}
