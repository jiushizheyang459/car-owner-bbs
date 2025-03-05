package com.ori.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 标识工具类
 */
public class IdentifierUtils {

    /**
     * 获取用户标识
     * 用户ID或IP地址
     *
     * @param request 用户请求
     * @return 用户唯一标识
     */
    public static String getUserIdentifier(HttpServletRequest request) {
        // 已登录用户使用用户ID
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "user:" + ((UserDetails)auth.getPrincipal()).getUsername();
        }

        // 未登录用户使用IP地址
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        return "ip:" + ip;
    }
}
