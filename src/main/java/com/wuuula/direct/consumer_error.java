package com.wuuula.direct;

import com.rabbitmq.client.*;

import java.io.IOException;

public class consumer_error {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("direct_lost", BuiltinExchangeType.DIRECT);

        String queue = channel.queueDeclare().getQueue();

        channel.queueBind(queue,"direct_logs","error");

        System.out.println("等待接收数据");

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody(), "UTF-8");
                String routingKey = delivery.getEnvelope().getRoutingKey();
                System.out.println("收到：" + routingKey + "-----" + msg);
            }
        };

        CancelCallback cancel = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };

        channel.basicConsume(queue,true,callback,cancel);
    }
}
