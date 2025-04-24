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
}
