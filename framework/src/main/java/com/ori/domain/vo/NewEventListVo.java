package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventListVo {

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
     * 举办地区
     */
    private String venue;

}
