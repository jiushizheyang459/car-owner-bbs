package com.ori.handler.webSocket;

import com.ori.domain.vo.NotificationVo;
import com.ori.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebSocketHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationService notificationService;

    /**
     * 发送通知给指定用户
     *
     * @param userId 用户ID
     * @param notification 通知内容
     */
    public void sendNotification(Long userId, NotificationVo notification) {
        try {
        String destination = "/user/" + userId + "/notification";
        log.info("Sending notification to user {}: {}", userId, notification);
        messagingTemplate.convertAndSend(destination, notification);
        } catch (Exception e) {
            log.error("发送通知消息失败", e);
        }
    }

    /**
     * 发送未读通知数给指定用户
     *
     * @param userId 用户ID
     */
    public void sendUnreadCount(Long userId) {
        try {
        Long unreadCount = notificationService.getUnreadCount(userId);
        String destination = "/user/" + userId + "/unread-count";
        log.info("Sending unread count to user {}: {}", userId, unreadCount);
            messagingTemplate.convertAndSend(destination, unreadCount.toString()); // 发送字符串形式以避免类型转换问题
        } catch (Exception e) {
            log.error("发送未读通知数量失败", e);
        }
    }
} 