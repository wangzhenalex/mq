package org.example.springbootrabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author : zhen wang
 * @date : 2024/4/13 09:52
 * @Version: 1.0
 * @Desc : 高级确认
 */
@Component
public class ConfirmConfig {
    // 交换机名称
    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";

    // 队列名称
    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";

    // RoutingKey
    public static final String CONFIRM_ROUTING_KEY = "confirm_routingKey";

    //声明交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    //声明队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    //绑定队列和交换机
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue queue,
                                        @Qualifier("confirmExchange") DirectExchange exchange) {
        return org.springframework.amqp.core.BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }
}
