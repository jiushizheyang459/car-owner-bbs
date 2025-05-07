package com.ori.handler.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ori.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            // 检查是否有当前用户上下文
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Long userId = SecurityUtils.getUserId();
                String username = SecurityUtils.getNickname();
                
                // 只有在有用户上下文时才填充用户信息
                if (userId != null) {
                    this.setFieldValByName("createById", userId, metaObject);
                    this.setFieldValByName("updateById", userId, metaObject);
                }
                
                if (username != null) {
                    this.setFieldValByName("createBy", username, metaObject);
                    this.setFieldValByName("updateBy", username, metaObject);
                }
            }
            
            // 无论是否有用户上下文，都设置创建和更新时间
            this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        } catch (Exception e) {
            // 处理可能的异常，只设置时间字段
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            // 检查是否有当前用户上下文
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Long userId = SecurityUtils.getUserId();
                String username = SecurityUtils.getNickname();
                
                // 只有在有用户上下文时才填充用户信息
                if (userId != null) {
                    this.setFieldValByName("updateById", userId, metaObject);
                }
                
                if (username != null) {
                    this.setFieldValByName("updateBy", username, metaObject);
            }
            }
            
            // 无论是否有用户上下文，都设置更新时间
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        } catch (Exception e) {
            // 处理可能的异常，只设置时间字段
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        }
    }
}
