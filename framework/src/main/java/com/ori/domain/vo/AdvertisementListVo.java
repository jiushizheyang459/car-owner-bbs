package com.ori.domain.vo;

import com.ori.domain.entity.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementListVo {

    /**
     * 广告ID
     */
    private Long id;

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
     * 描述
     */
    private String description;
}
