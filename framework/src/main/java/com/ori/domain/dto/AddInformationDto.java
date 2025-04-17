package com.ori.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "添加资讯dto")
public class AddInformationDto {

    /**
     * 标题
     */
    private String title;

    /**
     * 资讯内容
     */
    private String content;

    /**
     * 缩略图
     */
    private String thumbnail;
}
