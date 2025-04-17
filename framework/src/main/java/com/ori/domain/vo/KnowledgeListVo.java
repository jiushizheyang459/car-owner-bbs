package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeListVo {

    private Long id;

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
