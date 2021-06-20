package com.wuuula.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class producer {

    public static void main(String[] args) throws Exception{
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("workqueue",false,false,false,null);
        while(true){
            System.out.println("输入消息：");
            String msg = new Scanner(System.in).nextLine();
            // 如果输入的是”exit“则结束生产者进程
            if ("exit".equals(msg)){
                break;
            }
            channel.basicPublish("","workqueue",null,msg.getBytes());
            System.out.println("消息已发送：" + msg);
        }
        channel.close();
    }
}
