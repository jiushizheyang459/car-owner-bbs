package com.ori.domain.dto;

import com.ori.enums.LikeAction;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LikeMessageDto implements Serializable {
    private Long articleId;
    private Long userId;
    private LikeAction action;
    private LocalDateTime timestamp;
}
