package org.example.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootrabbitmq.config.DelayedQueueConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author : zhen wang
 * @date : 2024/4/12 21:25
 * @Version: 1.0
 * @Desc : 发消息的控制器
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 开始发消息
     *
     * @param message
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        //发送消息
        log.info("当前时间:{},发送一条信息给两个TTL队列：{}", LocalDateTime.now(), message);

        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列。" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列。" + message);
    }

    /**
     * 开始发消息-带有过期时间
     * todo 这里有一个问题，基于队列的死信，过期时间会不准确
     *
     * @param message
     */
    @GetMapping("/sendMsg/{message}/{ttlTime}")
    public void sendMsgWithTTL(@PathVariable("message") String message, @PathVariable("ttlTime") String ttlTime) {
        //发送消息
        log.info("当前时间:{},发送一条信息给1个TTL队列QC：{}，时长：{}", LocalDateTime.now(), message + "-" + ttlTime, ttlTime);

        rabbitTemplate.convertAndSend("X", "XC",  message + "-" + ttlTime, msg -> {
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    /**
     * 开始发消息-延迟消息
     *
     * @param message
     */
    @GetMapping("/sendDelayMsg/{message}/{ttlTime}")
    public void sendDelayMsgWithTTL(@PathVariable("message") String message, @PathVariable("ttlTime") Integer ttlTime) {
        //发送消息
        log.info("当前时间:{},发送一条信息给1个延迟消息：{}，时长：{}", LocalDateTime.now(), message + "-" + ttlTime, ttlTime);

        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE, DelayedQueueConfig.DELAYED_ROUTING_KEY,  message + "-" + ttlTime, msg -> {
            msg.getMessageProperties().setDelay(ttlTime);
            return msg;
        });
    }
}
