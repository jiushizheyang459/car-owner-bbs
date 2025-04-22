package com.ori.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ori.constants.SystemConstants;
import com.ori.consumer.LikeConsumer;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.Like;
import com.ori.mapper.ArticleMapper;
import com.ori.mapper.LikeMapper;
import com.ori.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

/**
 * 点赞定时任务
 */
@Component
public class LikeScheduledTask {

    @Autowired
    private LikeConsumer likeConsumer;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private LikeMapper likeMapper;

    /**
     * 定时处理缓冲区剩余消息
     * 每5秒处理一次点赞缓冲区中的剩余消息
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void processRemainingMessages() {
        likeConsumer.processBatch();
    }

    /**
     * 定期从MySQL同步数据到Redis
     * 每10分钟将有变更的点赞数据同步到 Redis
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void syncFromDatabaseToRedis() {
        // 从 Redis 中获取变更过的文章 ID 集合
        Set<String> changedArticleIdStrs = redisCache.getCacheSet(SystemConstants.LIKE_CHANGED_ARTICLE_KEY);
        if (changedArticleIdStrs == null || changedArticleIdStrs.isEmpty()) {
            return;
        }

        Set<Long> changedArticleIds = changedArticleIdStrs.stream()
                .map(Long::parseLong)
                .collect(Collectors.toSet());

        for (Long articleId : changedArticleIds) {
            // 查询数据库中的最新点赞数量
            Article article = articleMapper.selectOne(
                    new LambdaQueryWrapper<Article>()
                            .select(Article::getLikeCount)
                            .eq(Article::getId, articleId)
            );

            Long likeCount = article != null ? article.getLikeCount() : 0L;

            // 更新 Redis 点赞数量
            String countKey = SystemConstants.ARTICLE_LIKE_COUNT_KEY + articleId;
            redisCache.setCacheObject(countKey, String.valueOf(likeCount), SystemConstants.LIKE_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);

            // 更新点赞用户列表
            updateUserLikeStatus(articleId);

            // 从变更集合中移除该文章 ID（清除变更标记）
            redisCache.removeCacheSetValue(SystemConstants.LIKE_CHANGED_ARTICLE_KEY, String.valueOf(articleId));
        }
    }

    /**
     * 同步指定文章的点赞用户信息到 Redis
     */
    private void updateUserLikeStatus(Long articleId) {
        List<Long> likedUserIds = likeMapper.selectList(
                new LambdaQueryWrapper<Like>()
                        .eq(Like::getArticleId, articleId)
                        .eq(Like::getDelFlag, 0)
        ).stream().map(Like::getUserId).collect(Collectors.toList());

        String usersKey = SystemConstants.ARTICLE_LIKE_USERS_KEY + articleId;

        redisCache.deleteObject(usersKey); // 清除原集合

        if (!likedUserIds.isEmpty()) {
            Set<String> userIdSet = likedUserIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toSet());
            redisCache.setCacheSet(usersKey, userIdSet);
            redisCache.expire(usersKey, SystemConstants.LIKE_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        }
    }
}
