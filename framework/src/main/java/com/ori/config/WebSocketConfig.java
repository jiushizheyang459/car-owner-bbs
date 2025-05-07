package com.ori.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 设置消息代理的前缀，即如果消息的前缀是"/topic"，就会将消息转发给消息代理（broker）
        // 再由消息代理将消息广播给当前连接的客户端
        config.enableSimpleBroker("/topic", "/user");
        // 设置应用程序的目的前缀，即如果消息的前缀是"/app"，就会将消息路由到带有@MessageMapping注解的方法
        config.setApplicationDestinationPrefixes("/app");
        // 设置用户目的地前缀，用于点对点消息
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册一个STOMP协议的endpoint，并指定使用SockJS协议
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
        
        // 添加原生WebSocket支持
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*");
    }
} 