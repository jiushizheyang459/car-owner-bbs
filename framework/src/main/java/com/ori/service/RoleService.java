package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Role;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author leeway
 * @since 2025-04-27 17:06:55
 */
public interface RoleService extends IService<Role> {
    
    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色信息，如果不存在返回null
     */
    Role selectRoleByUserId(Long userId);
    
    /**
     * 根据用户ID查询角色权限字符串
     *
     * @param userId 用户ID
     * @return 角色权限字符串，如果不存在返回null
     */
    String selectRoleKeyByUserId(Long userId);

    /**
     * 根据用户ID查询角色名称
     *
     * @param userId 用户ID
     * @return 角色名称，如果不存在返回null
     */
    String selectRoleNameByUserId(Long userId);
}
