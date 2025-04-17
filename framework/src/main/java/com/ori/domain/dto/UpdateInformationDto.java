package com.ori.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "修改资讯dto")
public class UpdateInformationDto extends AddInformationDto {
    private Long id;
}
