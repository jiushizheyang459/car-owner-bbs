package com.ori.controller;

import com.ori.domain.vo.MenuVo;
import com.ori.service.MenuService;
import com.ori.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单控制器
 * 提供菜单相关的接口，包括刷新当前用户的菜单列表
 *
 * @author leeway
 * @since 2025-04-27 17:06:55
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 刷新并获取当前用户的菜单列表
     * 用于在用户权限变更后重新获取最新的菜单数据
     *
     * @return 最新的菜单列表
     */
    @GetMapping("/refresh")
    public List<MenuVo> refreshCurrentUserMenu() {
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();
        // 查询用户菜单
        return menuService.selectMenuByUserId(userId);
    }
} 