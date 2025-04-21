package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddEventDto;
import com.ori.domain.dto.UpdateEventDto;
import com.ori.domain.vo.*;
import com.ori.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动表(Event)表控制层
 *
 * @author leeway
 * @since 2025-04-16 22:50:02
 */
@RestController
@RequestMapping("event")
public class EventController {
    /**
     * 服务对象
     */
    @Autowired
    private EventService eventService;

    /**
     * 分页查询全部活动
     *
     * @param pageNum 多少页
     * @param size 每页多少条
     * @return 全部活动结果
     */
    @GetMapping("/eventList")
    public ResponseResult eventList(Integer pageNum, Integer size) {
        PageVo vo = eventService.eventList(pageNum, size);
        return ResponseResult.okResult(vo);
    }

    /**
     * 查询精选活动
     *
     * @return 4篇精选活动
     */
    @GetMapping("/hotEventList")
    public ResponseResult hotEventList() {
        List<HotEventListVo> vo = eventService.hotEventList();
        return ResponseResult.okResult(vo);
    }

    /**
     * 查询最新活动
     *
     * @return 8篇最新活动
     */
    @GetMapping("/newEventList")
    public ResponseResult newEventList() {
        List<NewEventListVo> vo = eventService.newEventList();
        return ResponseResult.okResult(vo);
    }

    /**
     * 根据活动ID查询活动详情
     *
     * @param id 活动ID
     * @return 活动明细
     */
    @GetMapping("{id}")
    public ResponseResult eventDetail(@PathVariable("id") Long id) {
        EventDetailVo vo = eventService.eventDetail(id);
        return ResponseResult.okResult(vo);
    }

    /**
     * 新增活动
     *
     * @param addEventDto 要新增活动数据
     * @return 新增结果
     */
    @PostMapping
    public ResponseResult addEvent(@RequestBody AddEventDto addEventDto) {
        eventService.addEvent(addEventDto);
        return ResponseResult.okResult();
    }

    /**
     * 修改活动
     *
     * @param updateEventDto 要修改的活动数据
     * @return 修改结果
     */
    @PutMapping
    public ResponseResult updateEvent(@RequestBody UpdateEventDto updateEventDto) {
        eventService.updateEvent(updateEventDto);
        return ResponseResult.okResult();
    }

    /**
     * 删除活动
     *
     * @param ids 活动ID集合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteEvent(@RequestParam List<Long> ids) {
        eventService.deleteEvent(ids);
        return ResponseResult.okResult();
    }
}

