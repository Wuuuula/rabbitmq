package com.wuuula.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Random;
import java.util.Scanner;

public class producer {

    public static void main(String[] args) throws Exception{
        String[] a = {"warning","error"};

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("direct_logs", BuiltinExchangeType.DIRECT);

        while(true){
            System.out.println("输入消息：");
            String msg = new Scanner(System.in).nextLine();
            if ("exit".equals(msg)){
                break;
            }

            // 随机产生日志级别
            String level = a[new Random().nextInt(a.length)];

            // 参数1：交换机名
            // 参数2：routingKey，路由键，这里用日志级别，如“error”，“warning”
            // 参数3：其他配置属性
            // 参数4：发布的消息数据
            channel.basicPublish("direct_logs",level,null,msg.getBytes());
            System.out.println("消息已发送：" + level + "-----" + msg);
        }

        channel.close();
    }
}
