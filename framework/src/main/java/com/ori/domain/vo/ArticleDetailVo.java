package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVo {
    /**
     * ID
     */
    private Long id;

    /**
     * 品牌名
     */
    private String categoryName;

    /**
     * 文章状态
     */
    private Integer status;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 作者昵称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 作者id
     */
    private Long createById;

    /**
     * 作者
     */
    private String createBy;

    /**
     * 创作时间
     */
    private LocalDateTime createTime;

    /**
     * 浏览量
     */
    private Long viewCount;

    /**
     * 是否收藏
     */
    private Boolean saveFlag;

    /**
     * 是否点赞
     */
    private Boolean likeFlag;

    /**
     * 点赞数量
     */
    private Long likeCount;
}
