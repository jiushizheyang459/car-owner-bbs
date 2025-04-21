package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddAnnouncementDto;
import com.ori.domain.dto.AddInformationDto;
import com.ori.domain.dto.UpdateAnnouncementDto;
import com.ori.domain.dto.UpdateInformationDto;
import com.ori.domain.vo.AnnouncementDetailVo;
import com.ori.domain.vo.AnnouncementListVo;
import com.ori.domain.vo.InformationDetailVo;
import com.ori.domain.vo.informationListVo;
import com.ori.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告表(Announcement)表控制层
 *
 * @author leeway
 * @since 2025-04-20 14:23:49
 */
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    /**
     * 服务对象
     */
    @Autowired
    private AnnouncementService announcementService;

    /**
     * 查询最新的5篇公告
     *
     * @return 最新的5篇公告
     */
    @GetMapping
    public ResponseResult announcementList() {
        List<AnnouncementListVo> vos = announcementService.announcementList();
        return ResponseResult.okResult(vos);
    }

    /**
     * 根据公告ID查询公告详情
     *
     * @param id 公告ID
     * @return 公告明细
     */
    @GetMapping("{id}")
    public ResponseResult announcementDetail(@PathVariable("id") Long id) {
        AnnouncementDetailVo vo = announcementService.announcementDetail(id);
        return ResponseResult.okResult(vo);
    }

    /**
     * 新增公告
     *
     * @param addAnnouncementDto 要新增公告数据
     * @return 新增结果
     */
    @PostMapping
    public ResponseResult addAnnouncement(@RequestBody AddAnnouncementDto addAnnouncementDto) {
        announcementService.addAnnouncement(addAnnouncementDto);
        return ResponseResult.okResult();
    }

    /**
     * 修改公告
     *
     * @param updateAnnouncementDto 要修改的公告数据
     * @return 修改结果
     */
    @PutMapping
    public ResponseResult updateAnnouncement(@RequestBody UpdateAnnouncementDto updateAnnouncementDto) {
        announcementService.updateAnnouncement(updateAnnouncementDto);
        return ResponseResult.okResult();
    }

    /**
     * 删除公告
     *
     * @param ids 公告ID集合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteAnnouncement(@RequestParam List<Long> ids) {
        announcementService.deleteAnnouncement(ids);
        return ResponseResult.okResult();
    }
}

