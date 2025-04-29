package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.entity.UserRole;
import com.ori.mapper.UserRoleMapper;
import com.ori.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author leeway
 * @since 2025-04-27 17:06:55
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
} 