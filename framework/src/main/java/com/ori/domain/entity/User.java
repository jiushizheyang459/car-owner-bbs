package com.ori.domain.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 用户表(User)表实体类
 *
 * @author leeway
 * @since 2025-02-08 11:21:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User {
    /**
    * 主键
    */    
    @TableId
    private Long id;

    /**
    * 用户名
    */    
    private String userName;
    
    /**
    * 昵称
    */    
    private String nickName;
    
    /**
    * 密码
    */    
    private String password;
    
    /**
    * 用户类型：0代表普通用户，1代表管理员
    */    
    private Integer type;
    
    /**
    * 账号状态（0正常 1停用）
    */    
    private Integer status;
    
    /**
    * 邮箱
    */    
    private String email;
    
    /**
    * 手机号
    */    
    private String phoneNumber;
    
    /**
    * 用户性别（0男，1女，2未知）
    */    
    private Integer sex;
    
    /**
    * 头像
    */    
    private String avatar;
    
    /**
    * 创建人
    */    
    private String createBy;
    
    /**
    * 创建人
    */    
    private Long createById;
    
    /**
    * 创建时间
    */    
    private LocalDateTime createTime;
    
    /**
    * 更新人
    */    
    private String updateBy;
    
    /**
    * 更新人
    */    
    private Long updateById;
    
    /**
    * 更新时间
    */    
    private LocalDateTime updateTime;
    
    /**
    * 删除标志（0代表未删除，1代表已删除）
    */    
    private Integer delFlag;
    
}

