package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class informationListVo {

    /**
     * 文章ID
     */
    private Long id;

    /**
     * 作者昵称
     */
    private String nickName;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 发布日期
     */
    private LocalDate createDate;

    /**
     * 发布时间
     */
    private LocalTime createTime;
}
