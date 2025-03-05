package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkVo {

    /**
     * ID
     */
    private Long id;

    /**
     * 友链名称
     */
    private String name;

    /**
     * logo
     */
    private String logo;

    /**
     * 网站地址
     */
    private String url;

    /**
     * 网站介绍
     */
    private String description;
}
