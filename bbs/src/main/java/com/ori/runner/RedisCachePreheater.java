package com.ori.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ori.constants.SystemConstants;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.Like;
import com.ori.mapper.LikeMapper;
import com.ori.service.ArticleService;
import com.ori.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class RedisCachePreheater {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private LikeMapper likeMapper;

    /**
     * 应用启动后执行缓存预热
     */
    @EventListener(ApplicationReadyEvent.class)
    public void preloadCache() {
        try {
            preloadViewCounts();
        } catch (Exception e) {
            log.error("预热文章浏览量失败", e);
        }

        try {
            preloadArticleLikes();
        } catch (Exception e) {
            log.error("预热点赞数据失败", e);
        }

        // 可拓展更多预热功能
    }

    /**
     * 预热文章浏览量
     */
    private void preloadViewCounts() {
        List<Article> articles = articleService.list(
                new LambdaQueryWrapper<Article>()
                        .select(Article::getId, Article::getViewCount)
        );

        // 根据访问量设置不同的过期时间
        articles.parallelStream().forEach(article -> {
            String countKey = SystemConstants.VIEW_COUNT_KEY + article.getId();
            int viewCount = article.getViewCount().intValue();
            redisCache.setCacheObject(countKey, viewCount);

            if (viewCount > SystemConstants.HOT_ARTICLE_VIEW_COUNT) {
                redisCache.expire(countKey, SystemConstants.HOT_ARTICLES_EXPIRE_DAYS, TimeUnit.DAYS);
            } else {
                redisCache.expire(countKey, SystemConstants.NORMAL_ARTICLES_EXPIRE_HOURS, TimeUnit.HOURS);
            }
        });
    }

    /**
     * 预热文章点赞数据
     */
    private void preloadArticleLikes() {
        // 查询前 20 个按点赞数排序的热门文章 ID
        List<Article> hotArticles = articleService.list(
                new LambdaQueryWrapper<Article>()
                        .select(Article::getId, Article::getLikeCount)
                        .orderByDesc(Article::getLikeCount)
                        .last("LIMIT 20")
        );

        if (hotArticles.isEmpty()) return;

        List<Long> hotArticleIds = hotArticles.stream()
                .map(Article::getId)
                .collect(Collectors.toList());

        // 批量查询点赞记录
        List<Like> allLikes = likeMapper.selectList(
                new LambdaQueryWrapper<Like>()
                        .in(Like::getArticleId, hotArticleIds)
                        .eq(Like::getDelFlag, 0)
        );

        // 根据 articleId 分组
        Map<Long, List<Like>> likesGrouped = allLikes.stream()
                .collect(Collectors.groupingBy(Like::getArticleId));

        for (Article article : hotArticles) {
            Long articleId = article.getId();
            List<Like> articleLikes = likesGrouped.getOrDefault(articleId, Collections.emptyList());

            // 缓存点赞数量
            String countKey = SystemConstants.ARTICLE_LIKE_COUNT_KEY + articleId;
            redisCache.setCacheObject(countKey, articleLikes.size(), SystemConstants.LIKE_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);

            // 缓存点赞用户 ID 列表
            Set<String> userIdSet = articleLikes.stream()
                    .map(like -> String.valueOf(like.getUserId()))
                    .collect(Collectors.toSet());

            if (!userIdSet.isEmpty()) {
                String usersKey = SystemConstants.ARTICLE_LIKE_USERS_KEY + articleId;
                redisCache.setCacheSet(usersKey, userIdSet);
                redisCache.expire(usersKey, SystemConstants.LIKE_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
            }
        }
    }
}
