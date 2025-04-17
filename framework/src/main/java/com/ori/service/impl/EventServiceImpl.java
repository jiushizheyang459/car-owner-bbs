package com.ori.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.AddEventDto;
import com.ori.domain.dto.UpdateEventDto;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.Event;
import com.ori.domain.entity.Information;
import com.ori.domain.entity.User;
import com.ori.domain.vo.*;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.EventMapper;
import com.ori.mapper.UserMapper;
import com.ori.service.EventService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 活动表(Event)表服务实现类
 *
 * @author leeway
 * @since 2025-04-16 22:50:02
 */
@Service("eventService")
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageVo eventList(Integer pageNum, Integer pageSize) {
        LocalDateTime now = LocalDateTime.now();
        // 创建 LambdaQueryWrapper 并设置查询条件
        LambdaQueryWrapper<Event> wrapper = new LambdaQueryWrapper<>();
        wrapper.gt(Event::getEndTime, now)       // 结束时间大于现在
                .lt(Event::getStartTime, now)     // 开始时间小于现在
                .orderByDesc(Event::getCreateTime);

        // 分页查询
        Page<Event> page = new Page<>(pageNum, pageSize);
        page(page, wrapper);

        // region 获取活动列表并转换为 EventListVo 列表
        List<EventListVo> eventListVos = page.getRecords().stream()
                .map(event -> {
                    String status = null;
                    // 当前时间 > 结束报名时间 状态：已结束
                    if (now.isAfter(event.getEndTime())) {
                        status = "已结束";
                    }
                    // 如果当前时间 < 开始报名时间 状态：未开始
                    if (now.isBefore(event.getStartTime())) {
                        status = "未开始";
                    }

                    // 如果当前时间 > 开始报名时间， 当前时间 < 结束报名时间 状态：正在进行
                    if (now.isAfter(event.getStartTime()) && now.isBefore(event.getEndTime())) {
                        status = "正在进行";
                    }

                    return new EventListVo(
                            event.getId(),
                            event.getTitle(),
                            event.getEventTime(),
                            event.getContent(),
                            event.getThumbnail(),
                            event.getStartTime(),
                            event.getEndTime(),
                            status,
                            event.getCreateBy(),
                            event.getType(),
                            event.getVenue()
                    );
                })
                .collect(Collectors.toList());
        // endregion

        return new PageVo(eventListVos, page.getTotal());
    }

    @Override
    public EventDetailVo eventDetail(Long id) {
        Event event = getById(id);
        if (event == null) {
            return null;
        }

        EventDetailVo vo = BeanCopyUtils.copyBean(event, EventDetailVo.class);

        LocalDateTime now = LocalDateTime.now();

        String status = null;
        // 当前时间 > 结束报名时间 状态：已结束
        if (now.isAfter(event.getEndTime())) {
            status = "已结束";
        }
        // 如果当前时间 < 开始报名时间 状态：未开始
        if (now.isBefore(event.getStartTime())) {
            status = "未开始";
        }

        // 如果当前时间 > 开始报名时间， 当前时间 < 结束报名时间 状态：正在进行
        if (now.isAfter(event.getStartTime()) && now.isBefore(event.getEndTime())) {
            status = "正在进行";
        }
        vo.setStatus(status);

        return vo;
    }

    @Override
    public void addEvent(AddEventDto addEventDto) {
        Event event = BeanCopyUtils.copyBean(addEventDto, Event.class);

        if (!StringUtils.hasText(event.getContent())) {
            throw new SystemException(AppHttpCodeEnum.EVENT_NOT_NULL);
        }
        save(event);
    }

    @Override
    public void updateEvent(UpdateEventDto updateEventDto) {
        Event event = getById(updateEventDto.getId());
        if (Objects.isNull(event)) {
            throw new SystemException(AppHttpCodeEnum.EVENT_NOT_FOUND);
        }
        event = BeanCopyUtils.copyBean(updateEventDto, Event.class);
        updateById(event);
    }

    @Override
    public void deleteEvent(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.EVENT_IDS_NOT_NULL);
        }
        lambdaUpdate()
                .set(Event::getDelFlag, 1)
                .in(Event::getId, ids);
    }
}
