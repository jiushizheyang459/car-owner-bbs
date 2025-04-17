package com.ori.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddLinkDto {

    /**
     * 友链名称
     */
    private String name;

    /**
     * 友链logo
     */
    private String logo;

    /**
     * 友链介绍
     */
    private String description;

    /**
     * 网站地址
     */
    private String url;
}
