package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.entity.RoleMenu;
import com.ori.mapper.RoleMenuMapper;
import com.ori.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author leeway
 * @since 2025-04-27 17:06:55
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
} 