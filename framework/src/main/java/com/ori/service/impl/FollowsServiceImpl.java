package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.Follows;
import com.ori.domain.entity.Save;
import com.ori.domain.entity.User;
import com.ori.domain.vo.FollowsListVo;
import com.ori.domain.vo.RecommendFollowsListVo;
import com.ori.domain.vo.UserInfoVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.FollowsMapper;
import com.ori.service.ArticleService;
import com.ori.service.FollowsService;
import com.ori.service.UserService;
import com.ori.utils.SecurityUtils;
import net.bytebuddy.implementation.bytecode.constant.IntegerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    @Autowired
    private ArticleService articleService;

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

        // 一次性查询所有用户，减少 N+1 查询问题
        Map<Long, User> userMap = userService.lambdaQuery()
                .in(User::getId, userIds)
                .list()
                .stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        // 组装返回数据
        return follows.stream().map(follow -> {
            FollowsListVo vo = new FollowsListVo();
            vo.setId(follow.getId());
            vo.setFollowerId(follow.getFollowerId());
            vo.setFollowerUser(userMap.get(follow.getFollowerId()).getNickName());
            vo.setFollowedId(follow.getFollowedId());
            vo.setFollowedUser(userMap.get(follow.getFollowedId()).getUserName());
            vo.setFollowedAvatar(userMap.get(follow.getFollowedId()).getAvatar());
            vo.setCreateTime(follow.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 新增关注
     *
     * @param followedId 被关注者id
     */
    @Transactional
    @Override
    public void addFollows(Long followedId) {
        Long userId = SecurityUtils.getUserId();
        
        // 检查是否已经关注过该用户
//        Follows existingFollow = lambdaQuery()
//                .eq(Follows::getFollowerId, userId)
//                .eq(Follows::getFollowedId, followedId)
//                .one();
        Follows existingFollow = getBaseMapper().selectExisting(userId, followedId);

        // 如果已经存在关注记录
        if (existingFollow != null) {
            // 如果当前状态是已关注，则抛出异常
            if (existingFollow.getDelFlag() == 0) {
                throw new SystemException(AppHttpCodeEnum.FOLLOWED_EXIST);
            }
            
            // 如果之前取消过关注（delFlag=1），则恢复关注状态
            boolean success = getBaseMapper().reFollows(existingFollow.getId());

            if (!success) {
                throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
            }
            return;
        }

        // 如果从未关注过，则创建新的关注记录
        Follows newFollow = new Follows();
        newFollow.setFollowerId(userId);
        newFollow.setFollowedId(followedId);
        newFollow.setDelFlag(0);
        
        boolean saved = save(newFollow);
        if (!saved) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 删除数据
     *
     * @param followedId 被关注者id
     */
    @Transactional
    @Override
    public void deleteFollows(Long followedId) {
        Long userId = SecurityUtils.getUserId();

        // 直接更新 `del_flag`，如果受影响行数为 0，说明本来就没关注
        boolean updated = lambdaUpdate()
                .set(Follows::getDelFlag, 1)
                .set(Follows::getUpdateTime, LocalDateTime.now())
                .eq(Follows::getFollowerId, userId)
                .eq(Follows::getFollowedId, followedId)
                .update();

        if (!updated) {
            throw new SystemException(AppHttpCodeEnum.FOLLOWER_EXIST);
        }
    }

    @Override
    public List<RecommendFollowsListVo> recommendFollowsList() {
        Long userId = SecurityUtils.getUserId();

        // 计算每个用户的总浏览量并存入 Map
        Map<User, Long> userViewCountMap = userService.lambdaQuery().list()
                .stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> articleService.lambdaQuery()
                                .eq(Article::getCreateById, user.getId())
                                .eq(Article::getStatus, 0)
                                .list()
                                .stream()
                                .mapToLong(Article::getViewCount)
                                .sum()
                ));

        // 按总浏览量降序排序
        List<Map.Entry<User, Long>> sortedUsers = userViewCountMap.entrySet().stream()
                .sorted(Map.Entry.<User, Long>comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // 取前 5 名
        List<RecommendFollowsListVo> vos = new ArrayList<>();
        int count = 0;
        for (Map.Entry<User, Long> entry : sortedUsers) {
            if (!entry.getKey().getId().equals(userId)) { // 排除当前用户
                RecommendFollowsListVo vo = new RecommendFollowsListVo(entry.getKey());
                
                // 查询当前用户是否已关注该推荐用户
                vo.setFollowsFlag(isFollowed(userId, entry.getKey().getId()));
                vos.add(vo);
                count++;
            }
            if (count == 5) break;
        }

        return vos;
    }
    
    /**
     * 判断当前用户是否已关注指定用户
     * 
     * @param followerId 关注者ID（当前用户）
     * @param followedId 被关注者ID
     * @return 是否已关注
     */
    private boolean isFollowed(Long followerId, Long followedId) {
        long followCount = lambdaQuery()
                .eq(Follows::getFollowerId, followerId)
                .eq(Follows::getFollowedId, followedId)
                .eq(Follows::getDelFlag, 0)
                .count();
        
        return followCount > 0;
    }
}
