package com.ori.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ori.domain.entity.Article;
import org.apache.ibatis.annotations.Param;

public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 点赞计数增加 delta
     */
    void incrementLikeCount(@Param("articleId") Long articleId, @Param("delta") int delta);
}
