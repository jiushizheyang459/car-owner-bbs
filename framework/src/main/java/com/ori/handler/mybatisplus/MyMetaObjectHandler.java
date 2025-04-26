package com.ori.handler.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ori.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = null;
        try {
            userId = SecurityUtils.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
            userId = -1L;//表示是自己创建
        }
        this.setFieldValByName("createBy", SecurityUtils.getNickname(), metaObject);
        this.setFieldValByName("createById", userId, metaObject);
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateBy", SecurityUtils.getNickname(), metaObject);
        this.setFieldValByName("updateById", userId, metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String nickname = null;
        Long userId = null;
        try {
            if (SecurityUtils.getLoginUser() != null) {
                nickname = SecurityUtils.getNickname();
                userId = SecurityUtils.getUserId();
            }
        } catch (Exception e) {
            // 如果拿不到用户，默认用 游客
            nickname = "游客";
            userId = -1L;
        }
        this.setFieldValByName("updateBy", nickname, metaObject);
        this.setFieldValByName("updateById", userId, metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
