package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.dto.AddAdvertisementDto;
import com.ori.domain.dto.UpdateAdvertisementDto;
import com.ori.domain.entity.Advertisement;
import com.ori.domain.entity.Information;
import com.ori.domain.vo.AdvertisementDetailVo;
import com.ori.domain.vo.AdvertisementListVo;
import com.ori.domain.vo.InformationDetailVo;
import com.ori.domain.vo.PageVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.AdvertisementMapper;
import com.ori.service.AdvertisementService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 广告表(Advertisement)表服务实现类
 *
 * @author leeway
 * @since 2025-04-05 01:13:55
 */
@Service
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementMapper, Advertisement> implements AdvertisementService {

    /**
     * 分页查询所有广告
     * 按创建时间降序排列
     * 只查询未删除的
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 分页查询结果
     */
    @Override
    public PageVo advertisementList(Integer pageNum, Integer pageSize) {

        Page<Advertisement> page = lambdaQuery()
                .orderByDesc(Advertisement::getCreateTime)
                .eq(Advertisement::getDelFlag, Boolean.FALSE)
                .page(new Page<>(pageNum, pageSize));

        List<Advertisement> advertisements = page.getRecords();

        List<AdvertisementListVo> vos = BeanCopyUtils.copyBeanList(advertisements, AdvertisementListVo.class);

        return new PageVo(vos, page.getTotal());
    }

    /**
     * 分页查询前端可显示的广告
     * 只查询未删除的
     * 查询大于开始时间小于等于结束时间的结果
     * 只查询状态是启用的
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 前端可显示的广告
     */
    @Override
    public PageVo displayAdvertisementList(Integer pageNum, Integer pageSize) {

        LocalDateTime nowTime = LocalDateTime.now();

        Page<Advertisement> page = lambdaQuery()
                .orderByAsc(Advertisement::getPriority)
                .eq(Advertisement::getDelFlag, Boolean.FALSE)
                .lt(Advertisement::getStartTime, nowTime)
                .ge(Advertisement::getEndTime, nowTime)
                .eq(Advertisement::getStatus, Boolean.FALSE)
                .page(new Page<>(pageNum, pageSize));

        List<Advertisement> advertisements = page.getRecords();

        List<AdvertisementListVo> vos = BeanCopyUtils.copyBeanList(advertisements, AdvertisementListVo.class);

        return new PageVo(vos, page.getTotal());
    }

    @Override
    public AdvertisementDetailVo advertisementDetail(Long id) {
        Advertisement advertisement = getById(id);
        if (advertisement == null) {
            return null;
        }

        AdvertisementDetailVo vo = BeanCopyUtils.copyBean(advertisement, AdvertisementDetailVo.class);

        return vo;
    }

    @Transactional
    @Override
    public void addAdvertisement(AddAdvertisementDto addAdvertisementDto) {
        Advertisement advertisement = BeanCopyUtils.copyBean(addAdvertisementDto, Advertisement.class);

        if (!StringUtils.hasText(advertisement.getContent())) {
            throw new SystemException(AppHttpCodeEnum.ADVERTISEMENT_NOT_NULL);
        }
        save(advertisement);
    }

    @Transactional
    @Override
    public void updateAdvertisement(UpdateAdvertisementDto updateAdvertisementDto) {
        Advertisement advertisement = getById(updateAdvertisementDto.getId());
        if (Objects.isNull(advertisement)) {
            throw new SystemException(AppHttpCodeEnum.ADVERTISEMENT_NOT_FOUND);
        }
        advertisement = BeanCopyUtils.copyBean(updateAdvertisementDto, Advertisement.class);
        updateById(advertisement);
    }

    @Transactional
    @Override
    public void deleteAdvertisement(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.ADVERTISEMENT_IDS_NOT_NULL);
        }
        lambdaUpdate()
                .set(Advertisement::getDelFlag, 1)
                .in(Advertisement::getId, ids);
    }
}
