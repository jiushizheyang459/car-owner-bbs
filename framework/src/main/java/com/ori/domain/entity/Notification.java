package com.ori.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 通知表(Notification)表实体类
 *
 * @author leeway
 * @since 2025-05-05 04:01:47
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_notification")
public class Notification {

    @TableId
    private Long id;

    /**
     * 接收通知的用户ID
     */
    private Long receiverId;

    /**
     * 触发通知的用户ID
     */
    private Long senderId;

    /**
     * 通知类型(1文章评论，2评论回复)
     */
    private Integer type;

    /**
     * 相关文章ID
     */
    private Long articleId;

    /**
     * 相关评论ID
     */
    private Long commentId;

    /**
     * 相关回复ID
     */
    private Long replyId;

    /**
     * 通知内容摘要
     */
    private String content;

    /**
     * 是否已读（0代表未读，1代表已读）
     */
    private Integer isRead;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}

