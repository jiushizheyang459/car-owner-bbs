package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotEventListVo {

    /**
     * ID
     */
    private Long id;

    /**
     * 作者
     */
    private String createBy;

    /**
     * 头像地址
     */
    private String avatar;

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

}
