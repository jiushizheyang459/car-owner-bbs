package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddEventDto;
import com.ori.domain.dto.UpdateEventDto;
import com.ori.domain.entity.Event;
import com.ori.domain.vo.EventDetailVo;
import com.ori.domain.vo.HotEventListVo;
import com.ori.domain.vo.NewEventListVo;
import com.ori.domain.vo.PageVo;

import java.util.List;


/**
 * 活动表(Event)表服务接口
 *
 * @author leeway
 * @since 2025-04-16 22:50:02
 */
public interface EventService extends IService<Event> {

    /**
     * 分页查询全部活动
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 全部活动结果
     */
    PageVo eventList(Integer pageNum, Integer pageSize);

    /**
     * 分页查询精选活动
     *
     * @return 精选活动
     */
    List<HotEventListVo> hotEventList();

    /**
     * 分页查询最新活动
     *
     * @return 最新活动
     */
    List<NewEventListVo> newEventList();


    /**
     * 根据活动ID查询活动详情
     *
     * @param id 活动ID
     * @return 活动明细
     */
    EventDetailVo eventDetail(Long id);

    /**
     * 新增活动
     *
     * @param addEventDto 要新增活动数据
     */
    void addEvent(AddEventDto addEventDto);

    /**
     * 修改活动
     *
     * @param updateEventDto 要修改的活动数据
     */
    void updateEvent(UpdateEventDto updateEventDto);

    /**
     * 删除活动
     *
     * @param ids 活动ID集合
     */
    void deleteEvent(List<Long> ids);
}
