package com.ori.controller;

import com.ori.annotation.SystemLog;
import com.ori.domain.ResponseResult;
import com.ori.domain.vo.NotificationVo;
import com.ori.domain.vo.PageVo;
import com.ori.service.NotificationService;
import com.ori.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 通知表(Notification)表控制层
 *
 * @author leeway
 * @since 2024-05-05
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 分页查询当前用户的通知列表
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 通知列表
     */
    @GetMapping("/notificationList")
    public ResponseResult list(Integer pageNum, Integer pageSize) {
        Long userId = SecurityUtils.getUserId();
        PageVo vo = notificationService.getNotificationList(userId, pageNum, pageSize);
        return ResponseResult.okResult(vo);
    }

    /**
     * 获取当前用户的未读通知数量
     *
     * @return 未读通知数量
     */
    @GetMapping("/unread/count")
    public ResponseResult getUnreadCount() {
        Long userId = SecurityUtils.getUserId();
        Long count = notificationService.getUnreadCount(userId);
        return ResponseResult.okResult(count);
    }

    /**
     * 标记通知为已读
     *
     * @param ids 通知ID列表
     * @return 操作结果
     */
    @PutMapping("/read")
    @SystemLog(businessName = "标记通知为已读")
    public ResponseResult markAsRead(@RequestBody List<Long> ids) {
        Long userId = SecurityUtils.getUserId();
        for (Long id : ids) {
            notificationService.markAsRead(id, userId);
        }
        return ResponseResult.okResult();
    }

    /**
     * 标记所有通知为已读
     *
     * @return 操作结果
     */
    @PutMapping("/read/all")
    @SystemLog(businessName = "标记所有通知为已读")
    public ResponseResult markAllAsRead() {
        Long userId = SecurityUtils.getUserId();
        notificationService.markAllAsRead(userId);
        return ResponseResult.okResult();
    }
} 