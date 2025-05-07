package com.ori.controller;

import com.ori.constants.SystemConstants;
import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddCommentDto;
import com.ori.domain.entity.Comment;
import com.ori.domain.vo.PageVo;
import com.ori.service.CommentService;
import com.ori.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评论表(Comment)表控制层
 *
 * @author leeway
 * @since 2025-02-08 14:05:10
 */
@RestController
@RequestMapping("/comment")
@Api(tags = "评论", description = "评论相关接口")
public class CommentController {
    /**
     * 服务对象
     */
    @Autowired
    private CommentService commentService;

    /**
     * 分页查询评论
     *
     * @param articleId
     * @param pageNum
     * @param size
     * @return 分页查询评论结果
     */
    @GetMapping("/commentList")
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer size) {
        PageVo vo = commentService.commentList(SystemConstants.ARTICLE_COMMENT,articleId,pageNum,size);
        return ResponseResult.okResult(vo);
    }

    /**
     * 发表评论
     *
     * @param addCommentDto 发表内容
     * @return 发表成功结果
     */
    @PostMapping
    public ResponseResult addComment(@RequestBody AddCommentDto addCommentDto) {
        commentService.addComment(addCommentDto);
        return ResponseResult.okResult();
    }

    /**
     * 分页查询友情链接的评论
     *
     * @param pageNum 页码
     * @param size 每页多少条
     * @return 分页查询结果
     */
    @GetMapping("/linkCommentList")
    @ApiOperation(value = "友联评论列表", notes = "获取一页友链评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页号"),
            @ApiImplicitParam(name = "size", value = "每页大小")
    })
    public ResponseResult linkCommentList(Integer pageNum, Integer size) {
        PageVo vo = commentService.commentList(SystemConstants.LINK_COMMENT, null, pageNum, size);
        return ResponseResult.okResult(vo);
    }

    /**
     * 查询文章的评论数量
     *
     * @param id 文章ID
     * @return 文章的评论数量
     */
    @GetMapping("/commentCount/{id}")
    public ResponseResult commentCount(@PathVariable("id") Long id) {
        Integer count = commentService.commentCount(id);
        return ResponseResult.okResult(count);
    }

    /**
     * 删除评论
     *
     * @param ids 评论ID集合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteComment(@RequestParam List<Long> ids) {
        commentService.deleteComment(ids);
        return ResponseResult.okResult();
    }
}

