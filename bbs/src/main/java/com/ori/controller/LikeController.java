package com.ori.controller;

import com.ori.domain.ResponseResult;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    /**
     * 切换文章点赞状态
     *
     * @param articleId 被点赞文章的ID
     * @param userId 点赞者ID
     * @return 是否点赞成功
     */
    @PostMapping("/{articleId}")
    public ResponseResult<Boolean> toggleLike(@PathVariable Long articleId, @RequestParam Long userId) {
        // 用户必须登录
        if (userId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        boolean isLiked = likeService.toggleLike(articleId, userId);
        return ResponseResult.okResult(isLiked);
    }

    /**
     * 获取文章点赞状态
     *
     * @param articleId 被点赞文章的ID
     * @param userId 点赞者ID
     * @return 文章是否点赞
     */
    @GetMapping("/status/{articleId}")
    public ResponseResult<Boolean> getLikeStatus(@PathVariable Long articleId, @RequestParam Long userId) {
        // 用户必须登录
        if (userId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        }

        boolean isLiked = likeService.isLiked(articleId, userId);
        return ResponseResult.okResult(isLiked);
    }

    /**
     * 获取文章点赞数
     *
     * @param articleId 被点赞文章的ID
     * @return 文章的点赞数量
     */
    @GetMapping("/count/{articleId}")
    public ResponseResult<Long> getLikeCount(@PathVariable Long articleId) {
        Long likeCount = likeService.getLikeCount(articleId);
        return ResponseResult.okResult(likeCount);
    }
}
