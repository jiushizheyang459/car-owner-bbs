package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Like;

public interface LikeService extends IService<Like> {

    /**
     * 切换文章点赞状态
     *
     * @param articleId 被点赞文章的ID
     * @param userId 点赞者ID
     * @return 是否点赞成功
     */
    boolean toggleLike(Long articleId, Long userId);

    /**
     * 获取文章点赞状态
     *
     * @param articleId 被点赞文章的ID
     * @param userId 点赞者ID
     * @return 文章是否点赞
     */
    boolean isLiked(Long articleId, Long userId);

    /**
     * 获取文章点赞数
     *
     * @param articleId 被点赞文章的ID
     * @return 文章的点赞数量
     */
    Long getLikeCount(Long articleId);
}
