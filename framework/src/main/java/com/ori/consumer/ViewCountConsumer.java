package com.ori.consumer;

import com.ori.constants.MqConstants;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.ViewCountMessageDto;
import com.ori.domain.entity.Article;
import com.ori.service.ArticleService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 消息消费者
 * 处理视图计数消息
 */
@Component
public class ViewCountConsumer {
    @Autowired
    private ArticleService articleService;

    // 暂存消息
    private final Queue<ViewCountMessageDto> messageBuffer = new ConcurrentLinkedQueue<>();


    @RabbitListener(queues = MqConstants.VIEW_COUNT_QUEUE)
    public void handleViewCountMessage(ViewCountMessageDto message, Channel channel,
                                       @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            // 添加到缓冲区
            synchronized (messageBuffer) {
                messageBuffer.add(message);

                // 达到批处理阈值时，批量更新数据库
                if (messageBuffer.size() >= SystemConstants.BATCH_SIZE)  {
                    processBatch();
                }
            }

            // 确认消息已处理
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // 消息处理失败时，拒绝消息并重新入队
            channel.basicNack(tag, false, true);
        }
    }

    // 批量处理消息
    @Transactional
    public void processBatch() {
        try {
            Map<Long, Integer> incrementMap = new HashMap<>();

            // 合并同一帖子的计数增量
            for (ViewCountMessageDto messageDto : messageBuffer) {
                incrementMap.merge(messageDto.getArticleId(), messageDto.getIncrement(), Integer::sum);
            }

            if (incrementMap.isEmpty()) {
                return;
            }

            // 查询当前 view_count
            List<Article> articles = articleService.lambdaQuery()
                    .select(Article::getId, Article::getViewCount)
                    .in(Article::getId, incrementMap.keySet())
                    .list();

            for (Article article : articles) {
                article.setViewCount(article.getViewCount() + incrementMap.get(article.getId()));
            }

            // 更新数据库
            articleService.updateBatchById(articles);
            // 成功后清空缓冲区
            messageBuffer.clear();
        } catch (Exception e) {
            // 失败时回滚事务
            throw new RuntimeException("批处理浏览量计数失败", e);
        }
    }
}