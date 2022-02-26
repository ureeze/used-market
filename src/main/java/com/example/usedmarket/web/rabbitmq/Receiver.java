package com.example.usedmarket.web.rabbitmq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class Receiver {

//    @RabbitListener(queues = RabbitConfig.queueName)
//    public void receiveMessage(Object message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
//        try {
//            System.out.println(message);
//            channel.basicAck(tag, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}