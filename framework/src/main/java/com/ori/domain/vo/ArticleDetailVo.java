package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVo {
    /**
     * ID
     */
    private String id;

    /**
     * 品牌名
     */
    private String categoryName;

    /**
     * 文章状态
     */
    private String status;

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
     * 作者
     */
    private String createBy;

    /**
     * 创作时间
     */
    private String createTime;

    /**
     * 浏览量
     */
    private String viewCount;
}
