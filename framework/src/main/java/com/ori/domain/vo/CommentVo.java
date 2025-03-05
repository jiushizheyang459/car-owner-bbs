package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {

    private Long id;

    /**
     * 评论的文章id
     */
    private Long articleId;

    /**
     * 根评论id
     */
    private Long rootId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 所回复的评论的发布者id
     */
    private Long toCommentUserId;

    /**
     * 所回复的评论的发布者名称
     */
    private String toCommentUserName;

    /**
     * 回复的评论id
     */
    private Long toCommentId;

    /**
     * 创建人ID
     */
    private Long createById;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 评论人
     */
    private String createBy;

    private List<CommentVo> children;
}
