package com.ori.utils;

import com.ori.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author leeway
 */
public class SecurityUtils {
    /**
     * 获取用户
     * @return 返回登录用户，如果未登录或在非Web上下文中，返回null
     */
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            return (LoginUser) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * 获取Authentication
     * @return 返回认证对象，如果不存在则返回null
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 判断当前用户是否为管理员
     * @return 如果是管理员返回true，否则返回false
     */
    public static Boolean isAdmin(){
        LoginUser loginUser = getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            Long id = loginUser.getUser().getId();
        return id != null && 1L == id;
        }
        return false;
    }

    /**
     * 获取当前登录用户ID
     * @return 返回用户ID，如果未登录则返回null
     */
    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            return loginUser.getUser().getId();
    }
        return null;
    }

    /**
     * 获取当前登录用户昵称
     * @return 返回用户昵称，如果未登录则返回null
     */
    public static String getNickname() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getNickName();
        }
        return null;
    }
}
