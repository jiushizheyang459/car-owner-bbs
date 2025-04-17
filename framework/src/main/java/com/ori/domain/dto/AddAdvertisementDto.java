package com.ori.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加资讯dto")
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
}
