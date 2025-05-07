package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Notification;
import com.ori.domain.vo.PageVo;

public interface NotificationService extends IService<Notification> {
    /**
     * 获取用户的通知列表
     *
     * @param userId   用户ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 分页通知列表
     */
    PageVo getNotificationList(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取用户未读通知数量
     *
     * @param userId 用户ID
     * @return 未读通知数量
     */
    Long getUnreadCount(Long userId);

    /**
     * 标记通知为已读
     *
     * @param notificationId 通知ID
     * @param userId        用户ID
     */
    void markAsRead(Long notificationId, Long userId);

    /**
     * 标记用户所有通知为已读
     *
     * @param userId 用户ID
     */
    void markAllAsRead(Long userId);
} 