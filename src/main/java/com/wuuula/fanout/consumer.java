package com.wuuula.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;

public class consumer {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("logs","fanout");

        // 自动生成队列名
        // 非持久，独占，自动删除
        String queue = channel.queueDeclare().getQueue();

        // 把该队列，绑定到 logs 交换机
        // 对于fanout类型的交换机，routingKey 会被忽略，不允许null值
        channel.queueBind(queue,"logs","");

        System.out.println("等待接收数据");

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody(),"UTF-8");
                System.out.println("收到：" + msg);
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
