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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    @Transactional(timeout = 300)
    public void syncViewCountToDatabase() {
        try {
            // 只从Redis获取所有浏览量key，而不是查询所有文章
            Set<String> viewCountKeys = (Set<String>) redisCache.keys(SystemConstants.VIEW_COUNT_KEY + "*");
            
            // 批量处理，避免一次处理过多数据
            List<List<String>> batches = splitIntoBatches(viewCountKeys);
            
            for (List<String> batch : batches) {
                processBatch(batch);
            }
        } catch (Exception e) {
            throw new RuntimeException("定时批处理浏览量计数失败", e);
        }
    }

    private void processBatch(List<String> keys) {
        // 从键名中提取文章ID
        List<Long> articleIds = keys.stream()
            .map(key -> Long.valueOf(key.substring(SystemConstants.VIEW_COUNT_KEY.length())))
            .collect(Collectors.toList());
        
        // 只查询需要同步的文章
        List<Article> articles = articleService.listByIds(articleIds);
        
        // 收集需要更新的文章
        List<Article> articlesToUpdate = new ArrayList<>();
        for (Article article : articles) {
            String countKey = SystemConstants.VIEW_COUNT_KEY + article.getId();
            Integer viewCount = redisCache.getCacheObject(countKey);

            if (viewCount != null && !viewCount.equals(article.getViewCount().intValue())) {
                article.setViewCount(viewCount.longValue());
                articlesToUpdate.add(article);
            }

            // 根据访问量设置不同的过期时间
            if (viewCount != null && viewCount > SystemConstants.HOT_ARTICLE_VIEW_COUNT) {
                redisCache.expire(countKey, SystemConstants.HOT_ARTICLES_EXPIRE_DAYS, TimeUnit.DAYS);
            } else {
                redisCache.expire(countKey, SystemConstants.NORMAL_ARTICLES_EXPIRE_HOURS, TimeUnit.HOURS);
            }
        }

        // 批量更新，而不是一个一个更新
        if (!articlesToUpdate.isEmpty()) {
            articleService.updateBatchById(articlesToUpdate);
        }
    }

    /**
     * 将Redis获取所有浏览量key分批
     *
     * @param keys Redis获取所有浏览量key
     * @return 分批后的结果
     */
    private List<List<String>> splitIntoBatches(Set<String> keys) {
        List<List<String>> batches = new ArrayList<>();
        List<String> currentBatch = new ArrayList<>();
        
        for (String key : keys) {
            currentBatch.add(key);
            if (currentBatch.size() == SystemConstants.VIEW_COUNT_SINGLE_SYNC_COUNT) {
                batches.add(currentBatch);
                currentBatch = new ArrayList<>();
            }
        }
        
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }
        
        return batches;
    }
} 