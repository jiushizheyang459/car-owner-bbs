package com.ori.service.impl;

import com.ori.domain.entity.LoginUser;
import com.ori.domain.entity.User;
import com.ori.domain.entity.UserRole;
import com.ori.domain.vo.MenuVo;
import com.ori.domain.vo.UserInfoVo;
import com.ori.domain.vo.UserLoginVo;
import com.ori.exception.SystemException;
import com.ori.service.LoginService;
import com.ori.service.MenuService;
import com.ori.service.UserRoleService;
import com.ori.utils.BeanCopyUtils;
import com.ori.utils.JwtUtil;
import com.ori.utils.RedisCache;
import com.ori.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;
    
    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleServiceImpl roleService;

    @Override
    public UserLoginVo login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //判断认证是否通过
        if (ObjectUtils.isEmpty(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }

        //获取userId 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        //把用户信息存入redis
        redisCache.setCacheObject("login:" + userId, loginUser);
        
        // 获取用户菜单
        List<MenuVo> menus = menuService.selectMenuByUserId(loginUser.getUser().getId());

        //把User转换为UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        String roleKey = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());
        userInfoVo.setRoleKey(roleKey);

        //把token、userinfo和menus封装 返回
        UserLoginVo vo = new UserLoginVo(jwt, userInfoVo, menus);
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
