package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.vo.FollowsListVo;
import com.ori.domain.vo.RecommendFollowsListVo;
import com.ori.domain.vo.UserInfoVo;
import com.ori.service.FollowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * (Follows)表控制层
 *
 * @author leeway
 * @since 2025-04-03 00:01:11
 */
@RestController
@RequestMapping("/follows")
public class FollowsController{
    /**
     * 服务对象
     */
    @Autowired
    private FollowsService followsService;

    /**
     * 查询当前用户关注的用户
     *
     * @return 被关注者昵称
     */
    @GetMapping
    public ResponseResult followsList() {
        List<FollowsListVo> vos = followsService.followsList();
        return ResponseResult.okResult(vos);
    }

    /**
     * 新增关注
     *
     * @param followedId 被关注者id
     */
    @PostMapping
    public ResponseResult addFollows(Long followedId) {
        followsService.addFollows(followedId);
        return ResponseResult.okResult();
    }

    /**
     * 取消关注
     *
     * @param followedId 被关注者id
     */
    @DeleteMapping
    public ResponseResult delete(Long followedId) {
        followsService.deleteFollows(followedId);
        return ResponseResult.okResult();
    }

    /**
     * 查询推荐关注
     *
     * @return 根据发布文章浏览量从高到低排序的前5个用户
     */
    @GetMapping("/recommendFollowsList")
    public ResponseResult recommendFollowsList() {
        List<RecommendFollowsListVo> vos = followsService.recommendFollowsList();
        return ResponseResult.okResult(vos);
    }
}

