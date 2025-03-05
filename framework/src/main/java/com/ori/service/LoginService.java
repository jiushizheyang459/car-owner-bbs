package com.ori.service;

import com.ori.domain.ResponseResult;
import com.ori.domain.entity.User;
import com.ori.domain.vo.UserLoginVo;

public interface LoginService {
    UserLoginVo login(User user);

    void logout();
}
