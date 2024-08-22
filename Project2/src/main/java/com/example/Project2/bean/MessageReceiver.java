package com.example.Project2.bean;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;
import com.rabbitmq.client.Channel;

@Component
public class MessageReceiver implements ChannelAwareMessageListener {

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String receivedMessage = new String(message.getBody());
            // 处理收到的消息
            System.out.println("Received message: " + receivedMessage);

            // 手动确认消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            // 处理失败时，拒绝消息并且不再重新投递
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
    }
}