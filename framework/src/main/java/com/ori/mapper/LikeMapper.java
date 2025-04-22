package com.ori.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ori.domain.entity.Like;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LikeMapper extends BaseMapper<Like> {
    /**
     * 查询某篇文章中指定用户的点赞记录
     */
    List<Like> selectByArticleAndUsers(@Param("articleId") Long articleId,
                                       @Param("userIds") List<Long> userIds);

    /**
     * 批量插入点赞记录
     */
    void batchInsert(@Param("likes") List<Like> likes);

    /**
     * 批量更新点赞记录的 delFlag 和时间
     */
    void batchUpdateDelFlagAndTime(@Param("articleId") Long articleId,
                                   @Param("likes") List<Like> likes);

    /**
     * 批量将点赞标记为取消点赞
     */
    void batchMarkUnlike(@Param("articleId") Long articleId,
                         @Param("userIds") List<Long> userIds);
}
