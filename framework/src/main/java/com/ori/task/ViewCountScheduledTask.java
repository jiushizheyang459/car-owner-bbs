package com.ori.task;

import com.ori.constants.SystemConstants;
import com.ori.domain.entity.Article;
import com.ori.service.ArticleService;
import com.ori.utils.RedisCache;
import com.ori.consumer.ViewCountConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文章访问量定时任务
 */
@Component
public class ViewCountScheduledTask {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ViewCountConsumer viewCountConsumer;

    /**
     * 定时处理消息缓冲区中的剩余消息
     * 每5秒检查一次
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void processRemainingMessages() {
        viewCountConsumer.processBatch();
    }

    /**
     * 定时将Redis中的浏览量同步到数据库
     * 每5分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void syncViewCountToDatabase() {
        try {
            // 从数据库获取所有文章的浏览量
            List<Article> articles = articleService.list();

            for (Article article : articles) {
                String countKey = SystemConstants.VIEW_COUNT_KEY + article.getId();
                Integer viewCount = redisCache.getCacheObject(countKey);

                if (viewCount != null && !viewCount.equals(article.getViewCount().intValue())) {
                    article.setViewCount(viewCount.longValue());
                    articleService.updateById(article);
                }

                // 根据访问量设置不同的过期时间
                if (viewCount != null && viewCount > SystemConstants.HOT_ARTICLE_VIEW_COUNT) {
                    redisCache.expire(countKey, SystemConstants.HOT_ARTICLES_EXPIRE_DAYS, TimeUnit.DAYS);
                } else {
                    redisCache.expire(countKey, SystemConstants.NORMAL_ARTICLES_EXPIRE_HOURS, TimeUnit.HOURS);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("定时批处理浏览量计数失败", e);
        }
    }
} 