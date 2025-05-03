package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddAnnouncementDto;
import com.ori.domain.dto.UpdateAnnouncementDto;
import com.ori.domain.entity.Announcement;
import com.ori.domain.vo.AnnouncementDetailVo;
import com.ori.domain.vo.AnnouncementListVo;
import com.ori.domain.vo.PageVo;

import java.util.List;


/**
 * 公告表(Announcement)表服务接口
 *
 * @author leeway
 * @since 2025-04-20 14:23:49
 */
public interface AnnouncementService extends IService<Announcement> {

    /**
     * 分页查询公告
     * 按时间降序
     * 只查询未删除的
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 按时间降序排列的分页查询结果
     */
    PageVo announcementList(Integer pageNum, Integer pageSize);

    /**
     * 分页查询可显示的公告
     * 按时间降序
     * 查询大于开始时间小于等于结束时间的结果
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 可显示的公告
     */
    PageVo displayAnnouncementList(Integer pageNum, Integer pageSize);

    /**
     * 根据公告ID查询公告详情
     *
     * @param id 公告ID
     * @return 公告明细
     */
    AnnouncementDetailVo announcementDetail(Long id);

    /**
     * 新增公告
     *
     * @param addAnnouncementDto 要新增公告数据
     */
    void addAnnouncement(AddAnnouncementDto addAnnouncementDto);

    /**
     * 修改公告
     *
     * @param updateAnnouncementDto 要修改的公告数据
     */
    void updateAnnouncement(UpdateAnnouncementDto updateAnnouncementDto);

    /**
     * 删除公告
     *
     * @param ids 公告ID集合
     */
    void deleteAnnouncement(List<Long> ids);
}
