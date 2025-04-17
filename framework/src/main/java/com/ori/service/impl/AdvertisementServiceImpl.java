package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.dto.AddAdvertisementDto;
import com.ori.domain.dto.UpdateAdvertisementDto;
import com.ori.domain.entity.Advertisement;
import com.ori.domain.entity.Information;
import com.ori.domain.vo.AdvertisementDetailVo;
import com.ori.domain.vo.AdvertisementListVo;
import com.ori.domain.vo.InformationDetailVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.AdvertisementMapper;
import com.ori.service.AdvertisementService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
     * 查询所有广告
     *
     * @return 所有广告数据
     */
    @Override
    public List<AdvertisementListVo> advertisementList() {

        List<Advertisement> advertisements = lambdaQuery()
                .orderByAsc(Advertisement::getPriority)
                .eq(Advertisement::getDelFlag, 0)
                .list();

        List<AdvertisementListVo> vos = BeanCopyUtils.copyBeanList(advertisements, AdvertisementListVo.class);

        return vos;
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

    @Override
    public void addAdvertisement(AddAdvertisementDto addAdvertisementDto) {
        Advertisement advertisement = BeanCopyUtils.copyBean(addAdvertisementDto, Advertisement.class);

        if (!StringUtils.hasText(advertisement.getContent())) {
            throw new SystemException(AppHttpCodeEnum.ADVERTISEMENT_NOT_NULL);
        }
        save(advertisement);
    }

    @Override
    public void updateAdvertisement(UpdateAdvertisementDto updateAdvertisementDto) {
        Advertisement advertisement = getById(updateAdvertisementDto.getId());
        if (Objects.isNull(advertisement)) {
            throw new SystemException(AppHttpCodeEnum.ADVERTISEMENT_NOT_FOUND);
        }
        advertisement = BeanCopyUtils.copyBean(updateAdvertisementDto, Advertisement.class);
        updateById(advertisement);
    }

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
