package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.User;
import com.ori.domain.vo.UserInfoVo;

import java.util.List;

public interface UserService extends IService<User> {

    /**
     * 查询个人信息
     *
     * @return 用户个人信息
     */
    UserInfoVo userInfo();

    void updateUserInfo(User user);

    void register(User user);

    /**
     * 查询推荐关注
     *
     * @return 根据发布文章浏览量从高到低排序的前5个用户
     */
    List<UserInfoVo> getRecommendedAttention();
}
