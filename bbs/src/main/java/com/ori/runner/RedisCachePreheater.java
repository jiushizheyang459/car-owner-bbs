package com.ori.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ori.constants.SystemConstants;
import com.ori.domain.entity.Article;
import com.ori.service.ArticleService;
import com.ori.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCachePreheater {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    // 应用启动后执行预热
    @EventListener(ApplicationReadyEvent.class)
    public void preloadViewCounts() {
        List<Article> articles = articleService.list();

        for (Article article : articles) {
            String countKey = SystemConstants.VIEW_COUNT_KEY + article.getId();
            int viewCount = article.getViewCount().intValue();
            redisCache.setCacheObject(countKey, viewCount);

            // 根据访问量设置不同的过期时间
            if (viewCount > SystemConstants.HOT_ARTICLE_VIEW_COUNT) {
                redisCache.expire(countKey, SystemConstants.HOT_ARTICLES_EXPIRE_DAYS, TimeUnit.DAYS);
            } else {
                redisCache.expire(countKey, SystemConstants.NORMAL_ARTICLES_EXPIRE_HOURS, TimeUnit.HOURS);
            }
        }

    }
}
