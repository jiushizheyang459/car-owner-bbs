package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.dto.AddAnnouncementDto;
import com.ori.domain.dto.UpdateAnnouncementDto;
import com.ori.domain.entity.Announcement;
import com.ori.domain.entity.Information;
import com.ori.domain.vo.AnnouncementDetailVo;
import com.ori.domain.vo.AnnouncementListVo;
import com.ori.domain.vo.InformationDetailVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.AnnouncementMapper;
import com.ori.service.AnnouncementService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 公告表(Announcement)表服务实现类
 *
 * @author leeway
 * @since 2025-04-20 14:23:49
 */
@Service("announcementService")
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {
    @Override
    public List<AnnouncementListVo> announcementList() {
        List<Announcement> announcements = lambdaQuery()
                .orderByDesc(Announcement::getCreateTime)
                .last("LIMIT 5")
                .list();
        List<AnnouncementListVo> vos = announcements.stream()
                .map(announcement -> {
                    LocalDate createDate = announcement.getCreateTime().toLocalDate();

                    return new AnnouncementListVo(
                            announcement.getId(),
                            announcement.getTitle(),
                            createDate
                    );
                })
                .collect(Collectors.toList());

        return vos;
    }

    @Override
    public AnnouncementDetailVo announcementDetail(Long id) {
        Announcement announcement = getById(id);
        if (announcement == null) {
            return null;
        }

        AnnouncementDetailVo vo = BeanCopyUtils.copyBean(announcement, AnnouncementDetailVo.class);

        return vo;
    }

    @Override
    public void addAnnouncement(AddAnnouncementDto addAnnouncementDto) {
        Announcement announcement = BeanCopyUtils.copyBean(addAnnouncementDto, Announcement.class);

        if (!StringUtils.hasText(announcement.getTitle())) {
            throw new SystemException(AppHttpCodeEnum.ANNOUNCEMENT_NOT_NULL);
        }
        save(announcement);
    }

    @Override
    public void updateAnnouncement(UpdateAnnouncementDto updateAnnouncementDto) {
        Announcement announcement = getById(updateAnnouncementDto.getId());
        if (Objects.isNull(announcement)) {
            throw new SystemException(AppHttpCodeEnum.ANNOUNCEMENT_NOT_FOUND);
        }
        announcement = BeanCopyUtils.copyBean(updateAnnouncementDto, Announcement.class);
        updateById(announcement);
    }

    @Override
    public void deleteAnnouncement(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.ANNOUNCEMENT_IDS_NOT_NULL);
        }
        lambdaUpdate()
                .set(Announcement::getDelFlag, 1)
                .in(Announcement::getId, ids);
    }
}
