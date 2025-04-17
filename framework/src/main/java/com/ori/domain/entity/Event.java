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
 * 活动表(Event)表实体类
 *
 * @author leeway
 * @since 2025-04-16 22:50:02
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_event")
public class Event {

    @TableId
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 活动报名开始时间
     */
    private LocalDateTime startTime;

    /**
     * 活动报名结束时间
     */
    private LocalDateTime endTime;

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

    /**
     * 活动时间
     */
    private LocalDateTime eventTime;

    /**
     * 活动类型
     */
    private String type;

    /**
     * 举办地区
     */
    private String venue;
}

