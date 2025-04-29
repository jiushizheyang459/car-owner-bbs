package com.ori.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.entity.Menu;
import com.ori.domain.entity.Role;
import com.ori.domain.entity.RoleMenu;
import com.ori.domain.entity.UserRole;
import com.ori.domain.vo.MenuVo;
import com.ori.mapper.MenuMapper;
import com.ori.service.MenuService;
import com.ori.service.RoleMenuService;
import com.ori.service.RoleService;
import com.ori.service.UserRoleService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author leeway
 * @since 2025-04-27 17:06:55
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private UserRoleService userRoleService;
    
    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RoleService roleService;

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Override
    public List<MenuVo> selectMenuByUserId(Long userId) {
        // 查询用户角色
        Role role = roleService.selectRoleByUserId(userId);
        if (role == null) {
            return new ArrayList<>();
        }
        
        // 查询角色菜单
        List<Long> menuIds = roleMenuService.lambdaQuery()
                .eq(RoleMenu::getRoleId, role.getId())
                .list()
                .stream()
                .map(RoleMenu::getMenuId)
                .distinct()
                .collect(Collectors.toList());
        
        if (menuIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 查询菜单
        List<Menu> menus = this.lambdaQuery()
                .in(Menu::getId, menuIds)
                .eq(Menu::getStatus, "0") // 正常状态
                .orderByAsc(Menu::getOrderNum)
                .list();
        
        // 构建菜单树
        return buildMenuTree(menus);
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由菜单
     */
    @Override
    public List<MenuVo> buildMenuTree(List<Menu> menus) {
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        
        // 构建树形结构
        List<MenuVo> menuTree = new ArrayList<>();
        
        // 先找出所有的一级菜单
        for (MenuVo menuVo : menuVos) {
            if (menuVo.getParentId() == 0L) {
                menuTree.add(menuVo);
            }
        }
        
        // 为一级菜单设置子菜单
        for (MenuVo menuVo : menuTree) {
            menuVo.setChildren(getChildList(menuVo.getId(), menuVos));
        }
        
        return menuTree;
    }
    
    /**
     * 递归查找子菜单
     *
     * @param id 菜单ID
     * @param menuVos 菜单列表
     * @return 子菜单列表
     */
    private List<MenuVo> getChildList(Long id, List<MenuVo> menuVos) {
        List<MenuVo> childList = new ArrayList<>();
        
        for (MenuVo menuVo : menuVos) {
            if (menuVo.getParentId().equals(id)) {
                childList.add(menuVo);
            }
        }
        
        // 递归查找子菜单的子菜单
        for (MenuVo menuVo : childList) {
            menuVo.setChildren(getChildList(menuVo.getId(), menuVos));
        }
        
        return childList;
    }

    /**
     * 根据角色ID查询菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    @Override
    public List<Long> selectMenuIdsByRoleId(Long roleId) {
        return roleMenuService.lambdaQuery()
                .eq(RoleMenu::getRoleId, roleId)
                .list()
                .stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }
}