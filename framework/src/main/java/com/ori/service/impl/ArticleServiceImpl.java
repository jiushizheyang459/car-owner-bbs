package com.ori.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.MqConstants;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.ViewCountMessageDto;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.Category;
import com.ori.domain.entity.User;
import com.ori.domain.vo.ArticleDetailVo;
import com.ori.domain.vo.ArticleListVo;
import com.ori.domain.vo.HotArticleVo;
import com.ori.domain.vo.PageVo;
import com.ori.mapper.ArticleMapper;
import com.ori.mapper.UserMapper;
import com.ori.service.ArticleService;
import com.ori.service.CategoryService;
import com.ori.utils.BeanCopyUtils;
import com.ori.utils.RedisCache;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查询热门文章
     *
     * @return 10篇热门文章
     */
    @Override
    public List<HotArticleVo> hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);

        Page<Article> page = new Page<>(1, 10);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

        List<HotArticleVo> vos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return vos;
    }

    /**
     * 分页查询文章
     *
     * @param pageNum
     * @param pageSize
     * @return 分页查询文章结果
     */
    @Override
    public PageVo articleList(Integer pageNum, Integer pageSize) {
        // 创建 LambdaQueryWrapper 并设置查询条件
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//        if (Objects.nonNull(categoryId) && categoryId > 0) {
//            wrapper.eq(Article::getCategoryId, categoryId);
//        }
        wrapper.orderByDesc(Article::getIsTop);
//        wrapper.orderByDesc(Article::getUpdateTime);

        // 分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        // 提取文章的 createById
        List<Long> userIds = page.getRecords().stream()
                .map(Article::getCreateById)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询所有用户信息
        List<User> users = userMapper.selectBatchIds(userIds);

        // 创建一个用户ID到用户信息的映射
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // region 获取文章列表并转换为 ArticleListVo 列表
        List<ArticleListVo> articleListVos = page.getRecords().stream()
                .map(article -> {
                    User user = userMap.getOrDefault(article.getCreateById(), new User());

                    return new ArticleListVo(
                            article.getId(),
                            user.getNickName(),
                            user.getAvatar(),
                            article.getTitle(),
                            article.getContent(),
                            Boolean.FALSE,
                            10L,
                            10L,
                            article.getViewCount(),
                            Boolean.FALSE
                    );
                })
                .collect(Collectors.toList());
        // endregion

        return new PageVo(articleListVos, page.getTotal());
    }

    /**
     * 根据文章ID查询文章详情
     *
     * @param id 文章ID
     * @return 文章明细
     */
    @Override
    public ArticleDetailVo articleDetail(Long id) {
        Article article = getById(id);
        if (article == null) {
            return null;
        }
        Integer viewCount = redisCache.getCacheObject(SystemConstants.VIEW_COUNT_KEY + id);
        if (viewCount == null) {
            viewCount = article.getViewCount().intValue();
            redisCache.setCacheObject(SystemConstants.VIEW_COUNT_KEY + id, viewCount);
        }
        article.setViewCount(viewCount.longValue());
        ArticleDetailVo vo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        Category category = categoryService.getById(article.getCategoryId());

        String categoryName = Optional.ofNullable(category).orElseGet(Category::new).getName();
        vo.setCategoryName(categoryName);

        return vo;
    }

    @Override
    public void updateViewCount(Long id, String userIdentifier) {
        // 检查是否是重复访问，保证幂等性
        boolean isNewView = checkAndMarkNewView(id, userIdentifier);

        // 如果是新访问，增加计数并发送消息
        if (isNewView) {
            incrementViewCount(id);
        }
    }

    /**
     * 检查并标记新访问，确保幂等性
     *
     * @param articleId 文章ID
     * @param userIdentifier 访问者标识
     * @return 是否为首次访问
     */
    private boolean checkAndMarkNewView(Long articleId, String userIdentifier) {
        String key = SystemConstants.ARTICLE_VISITED_KEY + articleId;
        // 使用SETNX命令确保幂等性
        Boolean isNewView = redisCache.setCacheSetValue(key, userIdentifier);

        redisCache.expire(key, SystemConstants.ARTICLE_VISITED_TIME, TimeUnit.MINUTES);

        return isNewView;
    }

    /**
     * 增加访问计数并发送消息
     *
     * @param articleId 文章ID
     */
    private void incrementViewCount(Long articleId) {
        String countKey = SystemConstants.VIEW_COUNT_KEY + articleId;

        // 原子性增加计数
        Long newCount = redisCache.incrementCacheObjectValue(countKey, 1);

        // 构建消息内容
        ViewCountMessageDto message = new ViewCountMessageDto();
        message.setArticleId(articleId);
        message.setIncrement(1);
        message.setTimestamp(LocalDateTime.now());

        // 发送消息到RabbitMQ
        rabbitTemplate.convertAndSend(
                MqConstants.VIEW_COUNT_EXCHANGE,
                MqConstants.VIEW_COUNT_ROUTING_KEY,
                message,
                messagePostProcessor -> {
                    // 设置消息持久化
                    messagePostProcessor.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return messagePostProcessor;
                }
        );

        // 根据浏览量设置不同的过期时间
        if (newCount != null) {
            if (newCount > SystemConstants.HOT_ARTICLE_VIEW_COUNT) {
                // 热门文章 - 7天过期
                redisCache.expire(countKey, SystemConstants.HOT_ARTICLES_EXPIRE_DAYS, TimeUnit.DAYS);
            } else {
                // 普通文章 - 24小时过期
                redisCache.expire(countKey, SystemConstants.NORMAL_ARTICLES_EXPIRE_HOURS, TimeUnit.HOURS);
            }
        }
    }
}
