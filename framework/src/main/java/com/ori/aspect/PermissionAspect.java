package com.ori.aspect;

import com.ori.annotation.HasPermission;
import com.ori.domain.entity.Menu;
import com.ori.domain.entity.Role;
import com.ori.domain.entity.RoleMenu;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.service.MenuService;
import com.ori.service.RoleMenuService;
import com.ori.service.RoleService;
import com.ori.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 权限拦截器
 *
 * @author leeway
 * @since 2025-04-27 17:06:55
 */
@Aspect
@Component
@Slf4j
public class PermissionAspect {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;
    
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 拦截带有@HasPermission注解的方法
     *
     * @param joinPoint 连接点
     */
    @Before("@annotation(com.ori.annotation.HasPermission)")
    public void doBefore(JoinPoint joinPoint) {
        // 获取当前登录用户ID
        Long userId = SecurityUtils.getUserId();
        
        // 获取用户角色权限字符串
        String roleKey = roleService.selectRoleKeyByUserId(userId);
        
        // 如果是管理员，直接放行
        if ("admin".equals(roleKey)) {
            return;
        }
        
        // 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        HasPermission hasPermission = method.getAnnotation(HasPermission.class);
        
        // 获取权限标识
        String permission = hasPermission.value();
        
        // 查询用户菜单
        List<Menu> menus = menuService.lambdaQuery()
                .eq(Menu::getPerms, permission)
                .list();
        
        // 如果菜单不存在，抛出异常
        if (menus.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        
        // 查询用户角色
        Role role = roleService.selectRoleByUserId(userId);
        if (role == null) {
            throw new SystemException(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
        
        // 查询角色菜单
        boolean hasMenuPermission = false;
        for (Menu menu : menus) {
            Long menuId = menu.getId();
            // 查询角色菜单关联
            List<RoleMenu> roleMenus = roleMenuService.lambdaQuery()
                    .eq(RoleMenu::getRoleId, role.getId())
                    .eq(RoleMenu::getMenuId, menuId)
                    .list();
            if (!roleMenus.isEmpty()) {
                hasMenuPermission = true;
                break;
            }
        }
        
        // 如果没有权限，抛出异常
        if (!hasMenuPermission) {
            throw new SystemException(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        }
    }
} 