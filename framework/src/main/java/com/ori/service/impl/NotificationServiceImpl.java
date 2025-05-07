package com.ori.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.SystemConstants;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.Notification;
import com.ori.domain.entity.User;
import com.ori.domain.vo.NotificationVo;
import com.ori.domain.vo.PageVo;
import com.ori.handler.webSocket.WebSocketHandler;
import com.ori.mapper.ArticleMapper;
import com.ori.mapper.NotificationMapper;
import com.ori.mapper.UserMapper;
import com.ori.service.NotificationService;
import com.ori.utils.BeanCopyUtils;
import com.ori.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Override
    public PageVo getNotificationList(Long userId, Integer pageNum, Integer pageSize) {

        // 分页查询通知
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getReceiverId, userId)
                .orderByDesc(Notification::getCreateTime);
        Page<Notification> page = new Page<>(pageNum, pageSize);

        page(page, queryWrapper);

        List<Notification> notifications = page.getRecords();
        if (CollectionUtils.isEmpty(notifications)) {
            return new PageVo(null, page.getTotal());
        }

        // 获取发送者ID信息
        List<Long> senderIds = notifications.stream()
                .map(Notification::getSenderId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<User> senders = userMapper.selectBatchIds(senderIds);
        Map<Long, User> userMap = senders.stream()
                .filter(Objects::nonNull) // 过滤掉 null 对象
                .collect(Collectors.toMap(
                        User::getId,            // key：User 的 id
                        Function.identity(),    // value：User 对象本身
                        (existing, replacement) -> existing // 如果 id 重复，保留第一个
                ));

        // 获取文章信息
        List<Long> articleIds = notifications.stream()
                .map(Notification::getArticleId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Article> articleMap = articleMapper.selectBatchIds(articleIds).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        Article::getId,
                        Function.identity(),
                        (existing, replacement) -> existing
                ));

        // 转换为VO
        List<NotificationVo> notificationVos = notifications.stream()
                .map(notification -> {
                    NotificationVo vo = BeanCopyUtils.copyBean(notification, NotificationVo.class);
                        User sender = userMap.get(notification.getSenderId());
                        vo.setSenderName(sender.getNickName());
                        vo.setSenderAvatar(sender.getAvatar());

                        Article article = articleMap.get(notification.getArticleId());
                        vo.setArticleTitle(article.getTitle());
                    return vo;
                }).collect(Collectors.toList());

        return new PageVo(notificationVos, page.getTotal());
    }

    @Override
    public Long getUnreadCount(Long userId) {
        String key = SystemConstants.UNREAD_COUNT_KEY + userId;
        Long unreadCount = redisCache.getCacheObject(key);
        if (unreadCount != null) {
            return unreadCount;
        }

        // 从数据库查询未读数
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getReceiverId, userId)
                .eq(Notification::getIsRead, SystemConstants.NOTIFICATION_UNREAD);
        Integer count = baseMapper.selectCount(queryWrapper);
        unreadCount = count != null ? count.longValue() : 0L;

        // 缓存到Redis
        redisCache.setCacheObject(key, unreadCount);
        return unreadCount;
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {
        // 更新通知状态
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getId, notificationId)
                .eq(Notification::getReceiverId, userId);

        Notification notification = new Notification();
        notification.setIsRead(SystemConstants.NOTIFICATION_READ);
        update(notification, queryWrapper);

        // 更新Redis中的未读数
        String key = SystemConstants.UNREAD_COUNT_KEY + userId;
        Object currentCount = redisCache.getCacheObject(key);
        if (currentCount != null) {
            // 确保减少未读数后不会小于0
            long newCount = Math.max(0, ((Number) currentCount).longValue() - 1);
            redisCache.setCacheObject(key, newCount);
        }

        // 推送更新后的未读通知数
        webSocketHandler.sendUnreadCount(userId);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        // 更新所有通知状态
        LambdaQueryWrapper<Notification> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notification::getReceiverId, userId)
                .eq(Notification::getIsRead, SystemConstants.NOTIFICATION_UNREAD);

        Notification notification = new Notification();
        notification.setIsRead(SystemConstants.NOTIFICATION_READ);
        update(notification, queryWrapper);

        // 更新Redis中的未读数
        String key = SystemConstants.UNREAD_COUNT_KEY + userId;
        redisCache.setCacheObject(key, 0L);

        // 推送更新后的未读通知数
        webSocketHandler.sendUnreadCount(userId);
    }
} 