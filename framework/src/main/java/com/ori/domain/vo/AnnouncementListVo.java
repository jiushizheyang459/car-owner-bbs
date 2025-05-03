package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementListVo {

    private Long id;

    private String title;

    private LocalDate creatDate;

    /**
     * 公告开始时间
     */
    private LocalDateTime startTime;

    /**
     * 公告结束时间
     */
    private LocalDateTime endTime;
}
