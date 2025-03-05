package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Comment;
import com.ori.domain.vo.PageVo;


/**
 * 评论表(Comment)表服务接口
 *
 * @author leeway
 * @since 2025-02-08 14:05:12
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页查询评论
     *
     * @param commentType
     * @param articleId
     * @param pageNum
     * @param size
     * @return 分页查询评论结果
     */
    PageVo commentList(String commentType, Long articleId, Integer pageNum, Integer size);

    /**
     * 添加评论
     *
     * @param comment
     */
    void addComment(Comment comment);
}
