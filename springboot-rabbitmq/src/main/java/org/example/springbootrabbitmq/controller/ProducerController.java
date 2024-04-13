package org.example.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.springbootrabbitmq.config.ConfirmConfig;
import org.springframework.amqp.core.Message;
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
 * @date : 2024/4/13 10:23
 * @Version: 1.0
 * @Desc : 发送
 */

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable("message") String message) {
        CorrelationData correlationData = new CorrelationData("id=uuid");
        //发送消息
        log.info("当前时间:{},发送高级确认消息：{}", LocalDateTime.now(), message);
//        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, message,correlationData);
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME + "_error", ConfirmConfig.CONFIRM_ROUTING_KEY, message,correlationData);
    }
}
