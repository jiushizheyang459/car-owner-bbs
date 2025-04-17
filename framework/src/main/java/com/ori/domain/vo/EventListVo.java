package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventListVo {

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 活动时间
     */
    private LocalDateTime eventTime;

    /**
     * 活动内容
     */
    private String content;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 活动报名开始时间
     */
    private LocalDateTime startTime;

    /**
     * 活动报名结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 活动类型
     */
    private String type;

    /**
     * 举办地区
     */
    private String venue;
}
