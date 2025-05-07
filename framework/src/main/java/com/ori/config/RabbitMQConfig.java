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

    // 定义文章浏览量队列
    @Bean
    public Queue viewCountQueue() {
        return new Queue(MqConstants.VIEW_COUNT_QUEUE, true); // true表示持久化
    }

    // 定义文章浏览量交换机
    @Bean
    public TopicExchange viewCountTopicExchange() {
        return new TopicExchange(MqConstants.VIEW_COUNT_EXCHANGE, true, false);
    }

    // 绑定文章浏览量队列和交换机
    @Bean
    public Binding viewCountBinding() {
        return BindingBuilder.bind(viewCountQueue()).to(viewCountTopicExchange()).with(MqConstants.VIEW_COUNT_ROUTING_KEY);
    }

    // 定义文章点赞队列
    @Bean
    public Queue likeQueue() {
        return new Queue(MqConstants.LIKE_QUEUE, true); // true表示持久化
    }

    // 定义文章点赞交换机
    @Bean
    public TopicExchange likeTopicExchange() {
        return new TopicExchange(MqConstants.LIKE_EXCHANGE, true, false);
    }

    // 绑定文章点赞队列和交换机
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(likeQueue()).to(likeTopicExchange()).with(MqConstants.LIKE_ROUTING_KEY);
    }

    // 定义通知队列
    @Bean
    public Queue notificationQueue() {
        return new Queue(MqConstants.NOTIFICATION_QUEUE, true);
    }

    // 定义通知交换机
    @Bean
    public TopicExchange notificationTopicExchange() {
        return new TopicExchange(MqConstants.NOTIFICATION_EXCHANGE, true, false);
    }

    // 绑定通知队列和交换机
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder.bind(notificationQueue()).to(notificationTopicExchange()).with(MqConstants.NOTIFICATION_ROUTING_KEY);
    }
}
