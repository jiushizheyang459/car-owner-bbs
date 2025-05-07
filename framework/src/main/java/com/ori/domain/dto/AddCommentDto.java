package com.ori.domain.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加评论dto")
public class AddCommentDto {

    /**
     * 评论类型（0代表文章评论，1代表友链评论）
     */
    @ApiModelProperty("评论类型（0代表文章评论，1代表友链评论）")
    private Integer type;

    /**
     * 评论的文章id
     */
    @ApiModelProperty(notes = "文章id")
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
    private String createBy;

    /**
     * 创建人ID
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
     * 更新人ID
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
