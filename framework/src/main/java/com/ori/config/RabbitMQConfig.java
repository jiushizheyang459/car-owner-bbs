package com.ori.config;

import com.ori.constants.MqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 定义队列
    @Bean
    public Queue viewCountQueue() {
        return new Queue(MqConstants.VIEW_COUNT_QUEUE, true); // true表示持久化
    }

    // 定义交换机
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(MqConstants.VIEW_COUNT_EXCHANGE, true, false);
    }

    // 绑定队列和交换机
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(viewCountQueue()).to(topicExchange()).with(MqConstants.VIEW_COUNT_ROUTING_KEY);
    }
}
