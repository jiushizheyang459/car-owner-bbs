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
        List<Comment> rootComments = page.getRecords();

        // 如果没查询到根评论，说明没有评论，直接返回
        if (rootComments.isEmpty()) {
            return new PageVo(Collections.emptyList(), 0L);
        }
        //endregion

        //region 获取根评论的 id 列表
        List<Long> rootCommentIds = rootComments.stream().map(Comment::getId).collect(Collectors.toList());
        //endregion

        //region 查询所有子评论（包括多层嵌套）
        LambdaQueryWrapper<Comment> allCommentsWrapper = new LambdaQueryWrapper<>();
        allCommentsWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId);
        allCommentsWrapper.eq(Comment::getType, commentType);
        allCommentsWrapper.eq(Comment::getDelFlag, 0);
        allCommentsWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> allComments = getBaseMapper().selectList(allCommentsWrapper);
        //endregion

        //region 批量查询用户信息
        List<Long> toCommentUserIds = allComments.stream()
                .map(Comment::getToCommentUserId)
                .filter(id -> id != -1L)
                .distinct()
                .collect(Collectors.toList());

        // 查询评论人ID列表
        List<Long> createByIds = allComments.stream()
                .map(Comment::getCreateById)
                .distinct()
                .collect(Collectors.toList());

        // 合并所有需要查询的用户ID
        List<Long> allUserIds = Stream.concat(toCommentUserIds.stream(), createByIds.stream())
                .distinct()
                .collect(Collectors.toList());

        Map<Long, User> userMap;
        // 用户ID不为空才查询
        if (!allUserIds.isEmpty()) {
            List<User> users = userMapper.selectBatchIds(allUserIds);
            userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
        } else {
            userMap = Collections.emptyMap();
        }
        //endregion

        //region 构建评论树
        // 将所有评论转换为CommentVo对象
        Map<Long, CommentVo> commentVoMap = new HashMap<>();
        for (Comment comment : allComments) {
            // 获取被回复用户信息
            User toCommentUser = userMap.getOrDefault(comment.getToCommentUserId(), new User());
            // 获取评论人信息
            User createUser = userMap.getOrDefault(comment.getCreateById(), new User());
            
            CommentVo commentVo = new CommentVo();
            commentVo.setId(comment.getId());
            commentVo.setArticleId(comment.getArticleId());
            commentVo.setRootId(comment.getRootId());
            commentVo.setContent(comment.getContent());
            commentVo.setToCommentUserId(comment.getToCommentUserId());
            commentVo.setToCommentUserName(toCommentUser.getNickName() == null ? "" : toCommentUser.getNickName());
            commentVo.setToCommentId(comment.getToCommentId());
            commentVo.setCreateById(comment.getCreateById());
            commentVo.setCreateTime(comment.getCreateTime());
            commentVo.setCreateBy(comment.getCreateBy());
            commentVo.setAvatar(createUser.getAvatar() == null ? "" : createUser.getAvatar());
            commentVo.setChildren(new ArrayList<>());
            
            commentVoMap.put(comment.getId(), commentVo);
        }

        // 构建评论树
        List<CommentVo> rootCommentVos = new ArrayList<>();
        for (Comment rootComment : rootComments) {
            CommentVo rootCommentVo = commentVoMap.get(rootComment.getId());
            if (rootCommentVo != null) {
                buildCommentTree(rootCommentVo, commentVoMap);
                rootCommentVos.add(rootCommentVo);
            }
        }
        //endregion

        return new PageVo(rootCommentVos, page.getTotal());
    }

    /**
     * 递归构建评论树
     *
     * @param parentCommentVo 父评论
     * @param commentVoMap 所有评论的Map
     */
    private void buildCommentTree(CommentVo parentCommentVo, Map<Long, CommentVo> commentVoMap) {
        // 查找所有回复当前评论的评论
        List<CommentVo> children = commentVoMap.values().stream()
                .filter(commentVo -> commentVo.getToCommentId() != null && 
                        commentVo.getToCommentId().equals(parentCommentVo.getId()))
                .collect(Collectors.toList());

        // 递归处理每个子评论
        for (CommentVo child : children) {
            buildCommentTree(child, commentVoMap);
        }

        // 设置子评论
        parentCommentVo.setChildren(children);
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
