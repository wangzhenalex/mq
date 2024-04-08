package org.example.rabbitmqhello.one;

import com.rabbitmq.client.ConnectionFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/4/8
 * @description：hello
 * @version: 1.0
 */
public class Producer {
//    队列名称
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
    }
}
