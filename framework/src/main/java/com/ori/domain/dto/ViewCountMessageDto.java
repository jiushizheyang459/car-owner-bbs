package com.ori.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ViewCountMessageDto implements Serializable {
    private Long articleId;
    private Integer increment;
    private LocalDateTime timestamp;
}