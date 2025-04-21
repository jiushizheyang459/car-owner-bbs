package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class newInformationListVo {
    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;
}
