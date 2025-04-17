package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddCommentDto;
import com.ori.domain.entity.Comment;
import com.ori.domain.vo.PageVo;

import java.util.List;


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
     * @param addCommentDto
     */
    void addComment(AddCommentDto addCommentDto);

    /**
     * 查询文章的评论数量
     *
     * @param id 文章ID
     * @return 文章的评论数量
     */
    Integer commentCount(Long id);

    /**
     * 删除评论
     *
     * @param ids 评论ID集合
     */
    void deleteComment(List<Long> ids);
}
