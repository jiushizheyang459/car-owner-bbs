package com.ori.consumer;

import com.ori.constants.MqConstants;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.NotificationMessageDto;
import com.ori.domain.entity.Notification;
import com.ori.domain.vo.NotificationVo;
import com.ori.handler.webSocket.WebSocketHandler;
import com.ori.mapper.NotificationMapper;
import com.ori.service.NotificationService;
import com.ori.utils.BeanCopyUtils;
import com.ori.utils.RedisCache;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RabbitListener(queues = MqConstants.NOTIFICATION_QUEUE)
@Slf4j
public class NotificationConsumer {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @RabbitListener(queues = MqConstants.NOTIFICATION_QUEUE)
    public void handleNotification(NotificationMessageDto messageDto, Channel channel,
                                 @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        log.info("收到通知消息：{}", messageDto);
        try {
            // 转换为通知实体并手动设置字段
            Notification notification = new Notification();
            notification.setReceiverId(messageDto.getReceiverId());
            notification.setSenderId(messageDto.getSenderId());
            notification.setType(messageDto.getType());
            notification.setArticleId(messageDto.getArticleId());
            notification.setCommentId(messageDto.getCommentId());
            notification.setReplyId(messageDto.getReplyId());
            notification.setContent(messageDto.getContent());
            notification.setIsRead(SystemConstants.NOTIFICATION_UNREAD); // 设置为未读
            notification.setCreateTime(LocalDateTime.now()); // 手动设置创建时间

            // 直接通过Mapper保存通知，避免触发ServiceImpl中可能存在的自动填充
            notificationMapper.insert(notification);

            // 更新Redis中的未读数
            String key = SystemConstants.UNREAD_COUNT_KEY + notification.getReceiverId();
            // 1. 先检查键是否存在
            Object currentCountObj = redisCache.getCacheObject(key);
            if (currentCountObj == null) {
                // 如果不存在，则从数据库获取未读计数并设置到Redis
                Long unreadCount = notificationService.getUnreadCount(notification.getReceiverId());
                redisCache.setCacheObject(key, unreadCount);
            } else {
                // 如果已存在，则增加计数
                // 确保类型转换正确
                long currentCount;
                if (currentCountObj instanceof Number) {
                    currentCount = ((Number) currentCountObj).longValue();
                    redisCache.setCacheObject(key, currentCount + 1);
                } else {
                    log.warn("Redis中的未读计数类型不是Number: {}", currentCountObj.getClass());
                    // 重新从数据库获取未读计数
                    Long unreadCount = notificationService.getUnreadCount(notification.getReceiverId());
                    redisCache.setCacheObject(key, unreadCount);
                }
            }

            // 转换为VO以发送到前端
            NotificationVo notificationVo = BeanCopyUtils.copyBean(notification, NotificationVo.class);

            // 通过WebSocket推送通知
            webSocketHandler.sendNotification(notification.getReceiverId(), notificationVo);
            
            try {
                // 推送未读通知数
                webSocketHandler.sendUnreadCount(notification.getReceiverId());
            } catch (Exception e) {
                log.error("推送未读通知数失败", e);
            }

            // 确认消息已处理
            channel.basicAck(tag, false);
            log.info("通知消息处理成功");
        } catch (Exception e) {
            log.error("通知消息处理失败", e);
            // 消息处理失败时，拒绝消息并重新入队
            channel.basicNack(tag, false, true);
        }
    }
} 