package com.ori.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddEventDto {

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
     * 活动开始时间
     */
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;

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
