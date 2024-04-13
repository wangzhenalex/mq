package org.example.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : zhen wang
 * @date : 2024/4/13 08:39
 * @Version: 1.0
 * @Desc : 延迟队列
 */
@Component
public class DelayedQueueConfig {
    //交换机
    public static final String DELAYED_EXCHANGE = "delayed.exchange";
    //队列
    public static final String DELAYED_QUEUE = "delayed.queue";
    //routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @Bean
    public Queue delayedQueue() {
        return QueueBuilder.durable(DELAYED_QUEUE).build();
    }

    //声明交换机
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false,
                map);
    }

    //绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                  @Qualifier("delayedExchange") CustomExchange exchange) {
        return BindingBuilder.bind(delayedQueue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
