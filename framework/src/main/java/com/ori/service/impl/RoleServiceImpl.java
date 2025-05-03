package com.ori.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.entity.Role;
import com.ori.domain.entity.UserRole;
import com.ori.mapper.RoleMapper;
import com.ori.service.RoleService;
import com.ori.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author leeway
 * @since 2025-04-27 17:06:55
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserRoleService userRoleService;

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色信息，如果不存在返回null
     */
    @Override
    public Role selectRoleByUserId(Long userId) {
        // 查询用户角色ID
        UserRole userRole = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, userId)
                .one();
        
        if (userRole == null) {
            return null;
        }
        
        // 查询角色信息
        return getById(userRole.getRoleId());
    }

    /**
     * 根据用户ID查询角色权限字符串
     *
     * @param userId 用户ID
     * @return 角色权限字符串，如果不存在返回null
     */
    @Override
    public String selectRoleKeyByUserId(Long userId) {
        // 查询用户角色ID
        UserRole userRole = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, userId)
                .one();
        
        if (userRole == null) {
            return null;
        }
        
        // 查询角色信息
        Role role = getById(userRole.getRoleId());
        if (role == null) {
            return null;
        }
        
        return role.getRoleKey();
    }

    /**
     * 根据用户ID查询角色名称
     *
     * @param userId 用户ID
     * @return 角色名称，如果不存在返回null
     */
    @Override
    public String selectRoleNameByUserId(Long userId) {
        // 查询用户角色ID
        UserRole userRole = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, userId)
                .one();

        if (userRole == null) {
            return null;
        }

        // 查询角色信息
        Role role = getById(userRole.getRoleId());
        if (role == null) {
            return null;
        }

        return role.getRoleName();
    }
}
