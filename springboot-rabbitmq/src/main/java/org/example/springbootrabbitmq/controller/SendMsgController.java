package org.example.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
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
}
