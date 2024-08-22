package com.example.Project2.bean;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue excelQueue() {
        return new Queue("excelQueue", true);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setConfirmCallback(confirmCallback());
        return rabbitTemplate;
    }

    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        return (correlationData, ack, cause) -> {
            if (ack) {
                System.out.println("Rabbitmq: Message sent successfully!");
            } else {
                System.out.println("Rabbitmq: Message sending failed: " + cause);
            }
        };
    }

    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(excelQueue());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 使用手动确认
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            try {
                // 处理消息
                String body = new String(message.getBody());
                System.out.println("Rabbitmq: Received: " + body);

                // 手动确认消息
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (Exception e) {
                // 处理失败，拒绝消息并重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
        });
        return container;
    }
}
