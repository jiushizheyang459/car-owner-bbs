package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationVo {
    /**
     * 通知ID
     */
    private Long id;

    /**
     * 发送者用户名
     */
    private String senderName;

    /**
     * 发送者头像
     */
    private String senderAvatar;

    /**
     * 通知类型(1文章评论，2评论回复)
     */
    private Integer type;

    /**
     * 相关文章ID
     */
    private Long articleId;

    /**
     * 相关文章标题
     */
    private String articleTitle;

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
    private LocalDateTime createTime;
} 