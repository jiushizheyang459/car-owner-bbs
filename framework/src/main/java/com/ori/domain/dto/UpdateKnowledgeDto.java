package com.ori.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateKnowledgeDto extends AddKnowledgeDto {

    /**
     * ID
     */
    private Long id;
}
