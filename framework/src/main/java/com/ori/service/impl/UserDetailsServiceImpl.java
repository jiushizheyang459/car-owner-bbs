package com.ori.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ori.domain.entity.LoginUser;
import com.ori.domain.entity.User;
import com.ori.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查询用户是否存在
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, username));

        if (ObjectUtils.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }

        //返回用户信息
        //TODO 查询权限信息封装
        return new LoginUser(user);
    }
}
