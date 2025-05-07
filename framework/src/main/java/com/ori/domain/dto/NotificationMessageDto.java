package com.ori.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessageDto implements Serializable {
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
} 