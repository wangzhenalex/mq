package org.example.springbootrabbitmq.callback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author : zhen wang
 * @date : 2024/4/13 10:50
 * @Version: 1.0
 * @Desc : 回调
 * 1、需要注入到rabbitTemplate
 * 2、需要配置进行开启confirm、return
 */
@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 注入
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 成功或者失败都会回调
     *
     * @param correlationData correlation data for the callback.
     * @param ack             true for ack, false for nack
     * @param cause           An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("交换机接收到消息,id:{}", null != correlationData ? correlationData.getId() : "");
        } else {
            log.error("交换机消息发送失败，原因：{},correlationData:{}", cause, correlationData);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        //接收到了回退消息
        log.error("消息退回，原因：{},message:{},exchange:{},routingKey:{}", replyText, message, exchange, routingKey);
    }
}
