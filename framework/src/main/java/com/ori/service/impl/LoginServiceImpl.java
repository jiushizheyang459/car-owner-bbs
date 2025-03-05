package com.ori.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.ori.domain.ResponseResult;
import com.ori.domain.entity.LoginUser;
import com.ori.domain.entity.User;
import com.ori.domain.vo.UserInfoVo;
import com.ori.domain.vo.UserLoginVo;
import com.ori.service.LoginService;
import com.ori.utils.BeanCopyUtils;
import com.ori.utils.JwtUtil;
import com.ori.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public UserLoginVo login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //判断认证是否通过
        if (ObjectUtils.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //获取userId 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        //把用户信息存入redis
        redisCache.setCacheObject("login:" + userId, loginUser);

        //把User转换为UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        //把token和userinfo封装 返回

        UserLoginVo vo = new UserLoginVo(jwt, userInfoVo);
        return vo;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        redisCache.deleteObject("login:" + userId);
    }
}
