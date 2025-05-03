package com.ori.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAnnouncementDto {
    private String title;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
