package org.example.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author : zhen wang
 * @date : 2024/4/13 12:14
 * @Version: 1.0
 * @Desc : 备份交换机
 */
@Component
@Slf4j
public class BackUpExChangeConfig {

    // 交换机名称
    public static final String BACKUP_EXCHANGE = "backup_exchange";
    // 队列名称
    public static final String BACKUP_QUEUE = "backup_queue";
    public static final String WARNING_QUEUE = "warning_queue";

    //声明交换机
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE);
    }

    //声明队列
    @Bean("backupQueue")
    public Queue backupQueue() {
        return new Queue(BACKUP_QUEUE);
    }

    @Bean("warningQueue")
    public Queue warningQueue() {
        return new Queue(WARNING_QUEUE);
    }

    //声明绑定关系
    @Bean
    public Binding queueBackUpBindingExchange(@Qualifier("backupQueue") Queue backupQueue,
                                        @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding queueWarningBindingExchange(@Qualifier("warningQueue") Queue warningQueue,
                                               @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
