package com.ori.consumer;

import com.ori.constants.MqConstants;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.LikeMessageDto;
import com.ori.domain.entity.Like;
import com.ori.enums.LikeAction;
import com.ori.mapper.ArticleMapper;
import com.ori.mapper.LikeMapper;
import com.ori.utils.RedisCache;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 点赞消息消费者：处理 RabbitMQ 点赞消息，批量入库，并记录变更文章ID以便定时任务同步到Redis。
 */
@Slf4j
@Component
public class LikeConsumer {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    private final Queue<LikeMessageDto> messageBuffer = new ConcurrentLinkedQueue<>();

    @RabbitListener(queues = MqConstants.LIKE_QUEUE)
    public void handleLikeMessage(LikeMessageDto message, Channel channel,
                                  @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            log.debug("收到点赞消息: {}", message);

            synchronized (messageBuffer) {
                messageBuffer.add(message);
                if (messageBuffer.size() >= SystemConstants.LIKE_BATCH_SIZE) {
                    processBatch();
                }
            }

            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("处理点赞消息失败", e);
            channel.basicNack(tag, false, true);
        }
    }

    public void processBatch() {
        if (messageBuffer.isEmpty()) return;

        Map<Long, List<LikeMessageDto>> articleMessages = messageBuffer.stream()
                .collect(Collectors.groupingBy(LikeMessageDto::getArticleId));

        Set<Long> changedArticleIds = new HashSet<>();

        for (Map.Entry<Long, List<LikeMessageDto>> entry : articleMessages.entrySet()) {
            Long articleId = entry.getKey();
            List<LikeMessageDto> messages = entry.getValue();

            Map<Long, LikeMessageDto> latestUserActions = messages.stream()
                    .collect(Collectors.toMap(
                            LikeMessageDto::getUserId,
                            m -> m,
                            (m1, m2) -> m1.getTimestamp().isAfter(m2.getTimestamp()) ? m1 : m2
                    ));

            processSinglePostLikes(articleId, latestUserActions.values());
            changedArticleIds.add(articleId);
        }

        // 记录变更文章ID到 Redis
        redisCache.setCacheSetValue(SystemConstants.LIKE_CHANGED_ARTICLE_KEY,
                changedArticleIds.stream().map(String::valueOf).toArray(String[]::new));

        messageBuffer.clear();
    }

    private void processSinglePostLikes(Long articleId, Collection<LikeMessageDto> messages) {
        int likeDelta = 0;
        List<Like> toInsert = new ArrayList<>();
        List<Like> toUpdate = new ArrayList<>();
        List<Long> unlikeUserIds = new ArrayList<>();
        List<Long> likeUserIds = new ArrayList<>();

        Map<Long, LikeMessageDto> likeMap = new HashMap<>();

        for (LikeMessageDto message : messages) {
            Long userId = message.getUserId();
            likeMap.put(userId, message);

            if (message.getAction() == LikeAction.LIKE) {
                likeUserIds.add(userId);
                likeDelta++;
            } else {
                unlikeUserIds.add(userId);
                likeDelta--;
            }
        }

        // 查询已有的点赞记录（只查一次）
        List<Like> existingLikes = Collections.emptyList();
        Set<Long> existingLikeUserIds = Collections.emptySet();
        
        // 只有当likeUserIds不为空时才查询数据库
        if (!likeUserIds.isEmpty()) {
            existingLikes = likeMapper.selectByArticleAndUsers(articleId, likeUserIds);
            existingLikeUserIds = existingLikes.stream()
                    .map(Like::getUserId).collect(Collectors.toSet());
        }

        for (Long userId : likeUserIds) {
            LikeMessageDto msg = likeMap.get(userId);
            Like like = new Like();
            like.setArticleId(articleId);
            like.setUserId(userId);
            like.setUpdateTime(msg.getTimestamp());
            like.setCreateTime(msg.getTimestamp());

            if (existingLikeUserIds.contains(userId)) {
                like.setDelFlag(0);
                toUpdate.add(like);
            } else {
                like.setDelFlag(0);
                toInsert.add(like);
            }
        }

        if (!toInsert.isEmpty()) {
            likeMapper.batchInsert(toInsert);
        }

        if (!toUpdate.isEmpty()) {
            likeMapper.batchUpdateDelFlagAndTime(articleId, toUpdate);
        }

        if (!unlikeUserIds.isEmpty()) {
            likeMapper.batchMarkUnlike(articleId, unlikeUserIds);
        }

        // 点赞数更新
        if (likeDelta != 0) {
            articleMapper.incrementLikeCount(articleId, likeDelta);
        }
    }
}

