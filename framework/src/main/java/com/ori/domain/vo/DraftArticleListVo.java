package com.ori.domain.vo;

import com.ori.domain.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DraftArticleListVo {
    /**
     * 文章ID
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 状态（0已发布，1草稿）
     */
    private Integer status;

    /**
     * 是否允许评论 1是，0否
     */
    private Integer isComment;
}
