package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.MqConstants;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.LikeMessageDto;
import com.ori.domain.entity.Like;
import com.ori.enums.LikeAction;
import com.ori.mapper.LikeMapper;
import com.ori.service.LikeService;
import com.ori.utils.RedisCache;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean toggleLike(Long articleId, Long userId) {
        String countKey = SystemConstants.ARTICLE_LIKE_COUNT_KEY + articleId;
        String usersKey = SystemConstants.ARTICLE_LIKE_USERS_KEY + articleId;

        // 使用Redis Lua脚本保证操作的原子性
        String script =
                "local isMember = redis.call('SISMEMBER', KEYS[2], ARGV[1]) " +
                        "local count " +
                        "if isMember == 1 then " +
                        "  redis.call('SREM', KEYS[2], ARGV[1]) " +
                        "  count = redis.call('DECR', KEYS[1]) " +
                        "  if count < 0 then " +
                        "    redis.call('SET', KEYS[1], '0') " +
                        "    count = 0 " +
                        "  end " +
                        "  return 0 " + // 返回0表示取消点赞
                        "else " +
                        "  redis.call('SADD', KEYS[2], ARGV[1]) " +
                        "  count = redis.call('INCR', KEYS[1]) " +
                        "  return 1 " + // 返回1表示点赞
                        "end";

        RedisScript<Long> redisScript = RedisScript.of(script, Long.class);

        // 执行Lua脚本
        Long result = redisCache.execute(
                redisScript,
                Arrays.asList(countKey, usersKey),
                userId.toString()
        );

        // 设置缓存过期时间
        redisCache.expire(countKey, SystemConstants.LIKE_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
        redisCache.expire(usersKey, SystemConstants.LIKE_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);

        // 创建消息对象
        LikeMessageDto message = new LikeMessageDto();
        message.setArticleId(articleId);
        message.setUserId(userId);
        message.setAction(result == 1 ? LikeAction.LIKE : LikeAction.UNLIKE);
        message.setTimestamp(LocalDateTime.now());

        // 发送消息到RabbitMQ
        rabbitTemplate.convertAndSend(
                MqConstants.LIKE_EXCHANGE,
                MqConstants.LIKE_ROUTING_KEY,
                message,
                messagePostProcessor -> {
                    messagePostProcessor.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return messagePostProcessor;
                }
        );

        return result == 1;
    }

    @Override
    public boolean isLiked(Long articleId, Long userId) {
        String usersKey = SystemConstants.ARTICLE_LIKE_USERS_KEY + articleId;
        Boolean isMember = redisCache.isSetMember(usersKey, userId.toString());
        return isMember != null && isMember;
    }

    @Override
    public Long getLikeCount(Long articleId) {
        String countKey = SystemConstants.ARTICLE_LIKE_COUNT_KEY + articleId;
        Object countObj = redisCache.getCacheObject(countKey);
        if (countObj == null) {
            // 从数据库加载点赞数
            Long count = lambdaQuery()
                    .eq(Like::getArticleId, articleId)
                    .eq(Like::getDelFlag, 0)
                    .count()
                    .longValue();
            redisCache.setCacheObject(countKey, String.valueOf(count), SystemConstants.LIKE_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
            return count;
        }
        
        // 处理不同类型的返回值
        if (countObj instanceof Integer) {
            return ((Integer) countObj).longValue();
        } else if (countObj instanceof String) {
            try {
                return Long.parseLong((String) countObj);
            } catch (NumberFormatException e) {
                // 如果缓存中的数据不是有效的数字，从数据库重新加载
                Long count = lambdaQuery()
                        .eq(Like::getArticleId, articleId)
                        .eq(Like::getDelFlag, 0)
                        .count()
                        .longValue();
                redisCache.setCacheObject(countKey, String.valueOf(count), SystemConstants.LIKE_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
                return count;
            }
        } else if (countObj instanceof Long) {
            return (Long) countObj;
        } else {
            // 未知类型，从数据库重新加载
            Long count = lambdaQuery()
                    .eq(Like::getArticleId, articleId)
                    .eq(Like::getDelFlag, 0)
                    .count()
                    .longValue();
            redisCache.setCacheObject(countKey, String.valueOf(count), SystemConstants.LIKE_CACHE_EXPIRE_DAYS, TimeUnit.DAYS);
            return count;
        }
    }
}
