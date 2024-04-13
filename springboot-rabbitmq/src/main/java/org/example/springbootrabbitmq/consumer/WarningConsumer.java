package org.example.springbootrabbitmq.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootrabbitmq.config.BackUpExChangeConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author : zhen wang
 * @date : 2024/4/13 14:43
 * @Version: 1.0
 * @Desc : 报警消费者
 */
@Component
@Slf4j
public class WarningConsumer {

    @RabbitListener(queues = BackUpExChangeConfig.BACKUP_QUEUE)
    public void receiveWarningMsg(Message message, Channel channel) {
        //打印消息
        log.info("报警：发现不可路由消息,{}" , new String(message.getBody()));
    }
}
