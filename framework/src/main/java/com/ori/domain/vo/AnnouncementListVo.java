package com.ori.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementListVo {

    private Long id;

    private String title;

    private LocalDate creatDate;
}
