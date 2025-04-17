package com.ori.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "修改广告dto")
public class UpdateAdvertisementDto extends AddAdvertisementDto {
    /**
     * ID
     */
    private Long id;
}
