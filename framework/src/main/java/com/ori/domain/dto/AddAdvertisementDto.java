package com.ori.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加广告dto")
public class AddAdvertisementDto {

    /**
     * 广告标题
     */
    private String title;

    /**
     * 广告内容
     */
    private String content;

    /**
     * 图片
     */
    private String img;

    /**
     * 广告优先级（越小越靠前）
     */
    private Integer priority;

    /**
     * 描述
     */
    private String description;

    /**
     * 跳转链接
     */
    private String link;

    /**
     * 状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 广告开始时间
     */
    private LocalDateTime startTime;

    /**
     * 广告结束时间
     */
    private LocalDateTime endTime;
}
