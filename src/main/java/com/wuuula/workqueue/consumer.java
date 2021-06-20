package com.wuuula.workqueue;

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

        channel.queueDeclare("workqueue",false,false,false,null);

//        channel.basicQos(1); // 一次只接收一条消息

        DeliverCallback callback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody(), "UTF-8");
                System.out.println("收到：" + msg);

                // 遍历字符串中的字符，每个点使进程暂停一秒
                for (int i = 0; i < msg.length(); i++) {
                    if (msg.charAt(i) == '.'){
                        try {
                            Thread.sleep(5000);
                        }catch (InterruptedException e){
                        }
                    }
                }
                System.out.println("处理结束");
//                // 发送回执
//                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };

        CancelCallback cancel = new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        };

        channel.basicConsume("workqueue",true,callback,cancel);
//        // 开启ack，即消息应答
//        channel.basicConsume("workqueue",false,callback,cancel);
    }
}
