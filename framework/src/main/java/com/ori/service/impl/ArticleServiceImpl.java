package com.ori.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.MqConstants;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.AddArticleDto;
import com.ori.domain.dto.UpdateArticleDto;
import com.ori.domain.dto.ViewCountMessageDto;
import com.ori.domain.entity.*;
import com.ori.domain.vo.*;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.ArticleMapper;
import com.ori.mapper.UserMapper;
import com.ori.service.ArticleService;
import com.ori.service.CategoryService;
import com.ori.service.CommentService;
import com.ori.service.FollowsService;
import com.ori.service.LikeService;
import com.ori.service.SaveService;
import com.ori.utils.BeanCopyUtils;
import com.ori.utils.RedisCache;
import com.ori.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private FollowsService followsService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private SaveService saveService;

    /**
     * 查询热门文章
     *
     * @return 10篇热门文章
     */
    @Override
    public List<HotArticleVo> hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.eq(Article::getDelFlag, 0);
        queryWrapper.orderByDesc(Article::getViewCount);

        Page<Article> page = new Page<>(1, 4);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

        List<Long> authorIds = articles.stream()
                .map(Article::getCreateById)
                .collect(Collectors.toList());

        List<User> users = userMapper.selectBatchIds(authorIds);

        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // region 获取文章列表并转换为 ArticleListVo 列表
        List<HotArticleVo> vos = articles.stream()
                .map(article -> {
                    User user = userMap.getOrDefault(article.getCreateById(), new User());

                    return new HotArticleVo(
                            article.getId(),
                            user.getNickName(),
                            user.getAvatar(),
                            article.getTitle(),
                            article.getContent(),
                            article.getThumbnail(),
                            article.getViewCount()
                    );
                })
                .collect(Collectors.toList());
        // endregion

        return vos;
    }

    /**
     * 查询最新文章
     *
     * @return 8篇最新文章
     */
    @Override
    public List<NewArticleVo> newArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.eq(Article::getDelFlag, 0);
        queryWrapper.orderByDesc(Article::getCreateTime);

        Page<Article> page = new Page<>(1, 8);
        page(page, queryWrapper);

        List<Article> articles = page.getRecords();

        List<Long> authorIds = articles.stream()
                .map(Article::getCreateById)
                .collect(Collectors.toList());

        List<User> users = userMapper.selectBatchIds(authorIds);

        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // region 获取文章列表并转换为 ArticleListVo 列表
        List<NewArticleVo> vos = articles.stream()
                .map(article -> {
                    User user = userMap.getOrDefault(article.getCreateById(), new User());
                    // 获取真实点赞数量
                    Long likeCount = likeService.getLikeCount(article.getId());

                    return new NewArticleVo(
                            article.getId(),
                            user.getNickName(),
                            user.getAvatar(),
                            article.getTitle(),
                            article.getContent(),
                            article.getThumbnail(),
                            article.getViewCount(),
                            likeCount
                    );
                })
                .collect(Collectors.toList());
        // endregion

        return vos;
    }

    /**
     * 分页全部查询文章
     *
     * @param pageNum
     * @param pageSize
     * @return 全部文章结果
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
                    Integer commentCount = commentService.commentCount(article.getId());
                    
                    // 获取点赞数量
                    Long likeCount = likeService.getLikeCount(article.getId());
                    
                    // 获取当前用户是否点赞
                    Boolean isLiked = Boolean.FALSE;
                    try {
                        Long currentUserId = SecurityUtils.getUserId();
                        isLiked = likeService.isLiked(article.getId(), currentUserId);
                    } catch (Exception e) {
                        log.debug("用户未登录，无法获取点赞状态");
                    }

                    // 获取当前用户是否收藏
                    Boolean isSaved = Boolean.FALSE;
                    try {
                        isSaved = saveService.isArticleSaved(article.getId());
                    } catch (Exception e) {
                        log.debug("用户未登录，无法获取收藏状态");
                    }

                    return new ArticleListVo(
                            article.getId(),
                            user.getNickName(),
                            user.getAvatar(),
                            article.getTitle(),
                            article.getContent(),
                            isLiked,
                            likeCount,
                            commentCount,
                            article.getViewCount(),
                            isSaved
                    );
                })
                .collect(Collectors.toList());
        // endregion

        return new PageVo(articleListVos, page.getTotal());
    }

    /**
     * 分页查询当前用户关注的用户发表的文章
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 当前用户关注的用户发表的文章
     */
    @Override
    public PageVo FollowArticleList(Integer pageNum, Integer pageSize) {

        Long userId = SecurityUtils.getUserId();

        // 获取当前用户关注的所有用户 ID
        List<Long> followedIds = followsService.lambdaQuery()
                .eq(Follows::getFollowerId, userId)
                .list()
                .stream()
                .map(Follows::getFollowedId)
                .collect(Collectors.toList());

        if (followedIds.isEmpty()) {
            return new PageVo(Collections.emptyList(), 0L);
        }

        // 分页查询关注的用户的文章
        Page<Article> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .in(Article::getCreateById, followedIds)
                .orderByDesc(Article::getIsTop)
                .orderByDesc(Article::getCreateTime);

        page(page, wrapper); // 调用分页

        List<Article> records = page.getRecords();

        if (records.isEmpty()) {
            return new PageVo(Collections.emptyList(), 0L);
        }

        // 批量查用户信息（避免 N 次 userMapper.selectById）
        Set<Long> userIds = records.stream()
                .map(Article::getCreateById)
                .collect(Collectors.toSet());

        List<User> users = userMapper.selectBatchIds(userIds);

        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // 封装 VO
        List<ArticleListVo> vos = records.stream().map(article -> {
            ArticleListVo vo = new ArticleListVo();
            vo.setId(article.getId());
            vo.setTitle(article.getTitle());
            vo.setContent(article.getContent());
            vo.setViewCount(article.getViewCount());
            
            // 获取真实点赞数量
            vo.setLikeCount(likeService.getLikeCount(article.getId()));
            
            // 获取当前用户是否点赞
            vo.setLikeFlag(likeService.isLiked(article.getId(), userId));
            
            vo.setCommentCount(commentService.commentCount(article.getId()));
            
            // 获取当前用户是否收藏
            vo.setSaveFlag(saveService.isArticleSaved(article.getId()));

            User author = userMap.get(article.getCreateById());
            if (author != null) {
                vo.setNickName(author.getNickName());
                vo.setAvatar(author.getAvatar());
            }

            return vo;
        }).collect(Collectors.toList());

        return new PageVo(vos, page.getTotal());
    }

    @Override
    public PageVo draftArticleList(Integer pageNum, Integer pageSize) {
        Long userId = SecurityUtils.getUserId();

        Page<Article> page = lambdaQuery()
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_DRAFT)
                .eq(Article::getCreateById, userId)
                .orderByDesc(Article::getIsTop)
                .page(new Page<>(pageNum, pageSize));

        List<Article> articles = page.getRecords();

        List<DraftArticleListVo> vos = BeanCopyUtils.copyBeanList(articles, DraftArticleListVo.class);

        return new PageVo(vos, page.getTotal());
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

        return buildArticleDetailVo(article);
    }

    private ArticleDetailVo buildArticleDetailVo(Article article) {
        ArticleDetailVo vo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);

        Category category = categoryService.getById(article.getCategoryId());
        String categoryName = Optional.ofNullable(category).orElseGet(Category::new).getName();
        vo.setCategoryName(categoryName);

        // 获取点赞数量
        Long likeCount = likeService.getLikeCount(article.getId());
        vo.setLikeCount(likeCount);
        
        // 获取当前用户是否点赞
        Boolean isLiked = Boolean.FALSE;
        try {
            Long currentUserId = SecurityUtils.getUserId();
            isLiked = likeService.isLiked(article.getId(), currentUserId);
        } catch (Exception e) {
            log.debug("用户未登录，无法获取点赞状态");
        }
        vo.setLikeFlag(isLiked);
        
        // 获取当前用户是否收藏
        Boolean isSaved = Boolean.FALSE;
        try {
            isSaved = saveService.isArticleSaved(article.getId());
        } catch (Exception e) {
            log.debug("用户未登录，无法获取收藏状态");
        }
        vo.setSaveFlag(isSaved);

        User user = userMapper.selectById(article.getCreateById());
        vo.setAvatar(user.getAvatar());

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

    @Override
    public void addArticle(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);

        if (!StringUtils.hasText(article.getContent())) {
            throw new SystemException(AppHttpCodeEnum.ARTICLE_NOT_NULL);
        }
        save(article);
    }

    @Override
    public void addDraftArticle(AddArticleDto addArticleDto) {
        Article article = BeanCopyUtils.copyBean(addArticleDto, Article.class);

        article.setStatus(1);

        save(article);
    }

    @Override
    public void updateArticle(UpdateArticleDto updateArticleDto) {
        Article article = getById(updateArticleDto.getId());
        if (Objects.isNull(article)) {
            throw new SystemException(AppHttpCodeEnum.ARTICLE_NOT_FOUND);
        }
        article = BeanCopyUtils.copyBean(updateArticleDto, Article.class);
        updateById(article);
    }

    @Override
    public void deleteArticle(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.ARTICLE_IDS_NOT_NULL);
        }
        lambdaUpdate()
                .set(Article::getDelFlag, 1)
                .in(Article::getId, ids);
    }

    @Override
    public Map<Long, ArticleDetailVo> getArticleDetailMap(List<Long> articleIds) {
        // 如果articleIds，返回空Map
        if (CollectionUtils.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }

        Map<Long, CompletableFuture<ArticleDetailVo>> futureMap = new HashMap<>();
        for (Long articleId : articleIds) {
            futureMap.put(articleId, CompletableFuture.supplyAsync(() -> articleDetail(articleId)));
        }

        Map<Long, ArticleDetailVo> result = new HashMap<>();
        for (Map.Entry<Long, CompletableFuture<ArticleDetailVo>> entry : futureMap.entrySet()) {
            try {
                ArticleDetailVo vo = entry.getValue().get();
                if (vo != null) {
                    result.put(entry.getKey(), vo);
                }
            } catch (Exception e) {
                log.error("获取文章详情失败，文章ID: {}", entry.getKey(), e);
            }
        }

        return result;
    }
}
