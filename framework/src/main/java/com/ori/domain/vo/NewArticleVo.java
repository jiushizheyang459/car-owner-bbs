package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewArticleVo {
    /**
     * ID
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
     * 标题
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
     * 访问量
     */
    private Long viewCount;
    /**
     * 点赞数量
     */
    private Long favour;
}
