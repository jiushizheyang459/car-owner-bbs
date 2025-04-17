package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InformationDetailVo {
    /**
     * ID
     */
    private Long id;
    /**
     * 文章状态
     */
    private Integer status;


    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 作者
     */
    private String createBy;

    /**
     * 创作时间
     */
    private LocalDateTime createTime;
}
