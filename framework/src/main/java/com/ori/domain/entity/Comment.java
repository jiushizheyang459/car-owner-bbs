package com.ori.domain.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 评论表(Comment)表实体类
 *
 * @author leeway
 * @since 2025-02-08 14:05:11
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_comment")
public class Comment {
    
    @TableId
    private Long id;

    /**
    * 评论类型（0代表文章评论，1代表友链评论）
    */    
    private Integer type;
    
    /**
    * 评论的文章id
    */    
    private Long articleId;
    
    /**
    * 根评论id
    */    
    private Long rootId;
    
    /**
    * 评论内容
    */    
    private String content;
    
    /**
    * 所回复的评论的发布者id
    */    
    private Long toCommentUserId;
    
    /**
    * 回复的评论id
    */    
    private Long toCommentId;
    
    /**
    * 创建人
    */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    /**
    * 创建人ID
    */
    @TableField(fill = FieldFill.INSERT)
    private Long createById;
    
    /**
    * 创建时间
    */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
    * 更新人
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateBy;
    
    /**
    * 更新人ID
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateById;
    
    /**
    * 更新时间
    */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
    * 删除标志（0代表未删除，1代表已删除）
    */    
    private Integer delFlag;
    
}

