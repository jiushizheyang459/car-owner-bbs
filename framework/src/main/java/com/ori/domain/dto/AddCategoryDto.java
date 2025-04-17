package com.ori.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryDto {

    /**
     * 分类名
     */
    private String name;

    /**
     * 描述
     */
    private String description;
}
