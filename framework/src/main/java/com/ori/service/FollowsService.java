package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Follows;
import com.ori.domain.vo.FollowsListVo;
import com.ori.domain.vo.RecommendFollowsListVo;

import java.util.List;


/**
 * (Follows)表服务接口
 *
 * @author leeway
 * @since 2025-04-03 00:01:11
 */
public interface FollowsService extends IService<Follows> {

    /**
     * 通过当前用户主键查询单条数据
     *
     * @return 被关注者昵称
     */
    List<FollowsListVo> followsList();

    /**
     * 新增关注
     *
     * @param followedId 被关注者id
     */
    void addFollows(Long followedId);

    /**
     * 取消关注
     *
     * @param followedId 被关注者id
     */
    void deleteFollows(Long followedId);

    /**
     * 查询推荐关注
     *
     * @return 根据发布文章浏览量从高到低排序的前5个用户
     */
    List<RecommendFollowsListVo> recommendFollowsList();
}
