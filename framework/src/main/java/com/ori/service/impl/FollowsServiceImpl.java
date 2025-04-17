package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.entity.Follows;
import com.ori.domain.entity.User;
import com.ori.domain.vo.FollowsListVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.FollowsMapper;
import com.ori.service.FollowsService;
import com.ori.service.UserService;
import com.ori.utils.SecurityUtils;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * (Follows)表服务实现类
 *
 * @author leeway
 * @since 2025-04-03 00:01:11
 */
@Service
public class FollowsServiceImpl extends ServiceImpl<FollowsMapper, Follows> implements FollowsService {

    @Autowired
    private UserService userService;

    /**
     * 通过当前用户主键查询单条数据
     *
     * @return 被关注者昵称
     */
    @Override
    public List<FollowsListVo> followsList() {
        Long userId = SecurityUtils.getUserId();

        // 查询关注列表
        List<Follows> follows = lambdaQuery()
                .eq(Follows::getFollowerId, userId)
                .list();
        if (follows.isEmpty()) {
            return Collections.emptyList();
        }

        // 获取所有相关用户 ID
        Set<Long> userIds = follows.stream()
                .flatMap(f -> Stream.of(f.getFollowerId(), f.getFollowedId()))
                .collect(Collectors.toSet());

        // 一次性查询所有用户昵称，减少 N+1 查询问题
        Map<Long, String> userNickNameMap = userService.lambdaQuery()
                .select(User::getId, User::getNickName)
                .in(User::getId, userIds)
                .list()
                .stream()
                .collect(Collectors.toMap(User::getId, User::getNickName));

        // 组装返回数据
        return follows.stream().map(follow -> {
            FollowsListVo vo = new FollowsListVo();
            vo.setId(follow.getId());
            vo.setFollowerId(follow.getFollowerId());
            vo.setFollowerUser(userNickNameMap.get(follow.getFollowerId()));
            vo.setFollowedId(follow.getFollowedId());
            vo.setFollowedUser(userNickNameMap.get(follow.getFollowedId()));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增数据
     *
     * @param followedId 被关注者id
     */
    @Override
    public void addFollows(Long followedId) {
        Long userId = SecurityUtils.getUserId();

        Follows existingFollow = lambdaQuery()
                .eq(Follows::getFollowerId, userId)
                .eq(Follows::getFollowedId, followedId)
                .one();

        // 已关注，直接抛异常
        if (existingFollow != null && existingFollow.getDelFlag() == 0) {
            throw new SystemException(AppHttpCodeEnum.FOLLOWED_EXIST);
        }

        // 如果曾取消关注，则恢复关注
        if (existingFollow != null) {
            boolean success = lambdaUpdate()
                    .set(Follows::getDelFlag, 0)
                    .eq(Follows::getFollowerId, userId)
                    .eq(Follows::getFollowedId, followedId)
                    .update();
            if (!success) {
                throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
            }
            return;
        }

        // 之前从未关注，插入新数据
        save(new Follows(userId, followedId));
    }

    /**
     * 删除数据
     *
     * @param followedId 被关注者id
     */
    @Override
    public void deleteFollows(Long followedId) {
        Long userId = SecurityUtils.getUserId();

        // 直接更新 `del_flag`，如果受影响行数为 0，说明本来就没关注
        boolean updated = lambdaUpdate()
                .set(Follows::getDelFlag, 1)
                .eq(Follows::getFollowerId, userId)
                .eq(Follows::getFollowedId, followedId)
                .eq(Follows::getDelFlag, 0) // 只更新已关注的记录
                .update();

        if (!updated) {
            throw new SystemException(AppHttpCodeEnum.FOLLOWER_EXIST);
        }
    }
}
