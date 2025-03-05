package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleListVo {

    /**
     * 文章ID
     */
    private Long id;

    /**
     * 作者昵称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 是否点赞
     */
    private Boolean favourFlag;

    /**
     * 点赞数量
     */
    private Long favourCount;

    /**
     * 评论数量
     */
    private Long commentCount;

    /**
     * 浏览数量
     */
    private Long viewCount;

    /**
     * 是否收藏
     */
    private Boolean saveFlag;
}
