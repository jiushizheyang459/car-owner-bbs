package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Menu;
import com.ori.domain.vo.MenuVo;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author leeway
 * @since 2025-04-27 17:06:55
 */
public interface MenuService extends IService<Menu> {
    
    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<MenuVo> selectMenuByUserId(Long userId);
    
    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由菜单
     */
    List<MenuVo> buildMenuTree(List<Menu> menus);
    
    /**
     * 根据角色ID查询菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(Long roleId);
}
