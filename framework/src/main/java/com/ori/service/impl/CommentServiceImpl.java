package com.ori.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.AddCommentDto;
import com.ori.domain.entity.Category;
import com.ori.domain.entity.Comment;
import com.ori.domain.entity.User;
import com.ori.domain.vo.CommentVo;
import com.ori.domain.vo.PageVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.CommentMapper;
import com.ori.mapper.UserMapper;
import com.ori.service.CommentService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author leeway
 * @since 2025-02-08 14:05:12
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询评论
     *
     * @param commentType
     * @param articleId
     * @param pageNum
     * @param size
     * @return 分页查询评论结果
     */
    @Override
    public PageVo commentList(String commentType, Long articleId, Integer pageNum, Integer size) {
        //region 查询根评论（root_id = -1）
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);
        wrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT);
        wrapper.eq(Comment::getType, commentType);

        Page<Comment> page = new Page<>(pageNum, size);
        page(page, wrapper);
        List<Comment> comments = page.getRecords();

        // 如果没查询到根评论，说明没有评论，直接返回
        if (comments.isEmpty()) {
            return new PageVo(Collections.emptyList(), 0L);
        }
        //endregion

        //region 获取根评论的 id 列表
        List<Long> rootCommentIds = comments.stream().map(Comment::getId).collect(Collectors.toList());
        //endregion

        //region 查询所有子评论
        LambdaQueryWrapper<Comment> childWrapper = new LambdaQueryWrapper<>();
        childWrapper.in(Comment::getRootId, rootCommentIds);
        childWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> childComments = getBaseMapper().selectList(childWrapper);
        //endregion

        //region 批量查询用户信息
        // 合并流 统一处理toCommentUserId
        List<Long> toCommentUserIds = Stream.concat(comments.stream(), childComments.stream())
                .map(Comment::getToCommentUserId)
                .filter(id -> id != -1L)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, User> userMap;
        //toCommentUserId不为空才查询
        if (!toCommentUserIds.isEmpty()) {
            List<User> users = userMapper.selectBatchIds(toCommentUserIds);
            userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        } else {
            userMap = Collections.emptyMap();
        }
        //endregion

        //region 组装子评论 Map，按 root_id 分组
        Map<Long, List<Comment>> childCommentMap = childComments.stream()
                .collect(Collectors.groupingBy(Comment::getRootId));
        //endregion

        //region 转换为 VO 对象
        List<CommentVo> commentVos = comments.stream()
                .map(comment -> {
                    User user = userMap.getOrDefault(comment.getToCommentUserId(), new User());
                    CommentVo commentVo = new CommentVo(
                            comment.getId(),
                            comment.getArticleId(),
                            comment.getRootId(),
                            comment.getContent(),
                            comment.getToCommentUserId(),
                            user.getNickName() == null ? "" : user.getNickName(),
                            comment.getToCommentId(),
                            comment.getCreateById(),
                            comment.getCreateTime(),
                            comment.getCreateBy(),
                            new ArrayList<>()
                    );

                    // 绑定子评论
                    List<CommentVo> children = childCommentMap.getOrDefault(comment.getId(), Collections.emptyList())
                            .stream()
                            .map(child -> {
                                User childUser = userMap.getOrDefault(child.getToCommentUserId(), new User());
                                return new CommentVo(
                                        child.getId(),
                                        child.getArticleId(),
                                        child.getRootId(),
                                        child.getContent(),
                                        child.getToCommentUserId(),
                                        childUser.getNickName() == null ? "" : childUser.getNickName(),
                                        child.getToCommentId(),
                                        child.getCreateById(),
                                        child.getCreateTime(),
                                        child.getCreateBy(),
                                        new ArrayList<>()
                                );
                            })
                            .collect(Collectors.toList());

                    commentVo.setChildren(children);
                    return commentVo;
                })
                .collect(Collectors.toList());
        //endregion

        return new PageVo(commentVos, page.getTotal());
    }

    @Override
    public void addComment(AddCommentDto addCommentDto) {
        Comment comment = BeanCopyUtils.copyBean(addCommentDto, Comment.class);

        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
    }

    @Override
    public Integer commentCount(Long id) {
        Integer count = lambdaQuery()
                .eq(Comment::getArticleId, id)
                .eq(Comment::getType, 0)
                .eq(Comment::getDelFlag, 0)
                .count();
        return count;
    }

    @Override
    public void deleteComment(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.COMMENT_IDS_NOT_NULL);
        }
        lambdaUpdate()
                .set(Comment::getDelFlag, 1)
                .in(Comment::getId, ids);
    }
}
