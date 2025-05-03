package com.ori.domain.vo;

import com.ori.domain.entity.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
