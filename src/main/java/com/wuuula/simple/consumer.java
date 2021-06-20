package com.wuuula.simple;

import com.rabbitmq.client.*;

import java.io.IOException;

public class consumer {

    public static void main(String[] args) throws Exception{
        // 连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        // 建立连接
        Connection connection = connectionFactory.newConnection();
        // 建立通信
        Channel channel = connection.createChannel();
        // 声明队列，如果该队列已经创建过，则不会重复创建
        channel.queueDeclare("helloworld",false,false,false,null);
        System.out.println("等待接收数据");

        // 收到消息后用来处理消息的回调对象
        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody(), "UTF-8");
                System.out.println("收到：" + msg);
            }
        };

        // 消费者取消时的回调对象
        CancelCallback cancel = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };
        channel.basicConsume("helloworld",true,callback,cancel);
    }
}
