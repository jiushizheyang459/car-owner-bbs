package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddAdvertisementDto;
import com.ori.domain.dto.UpdateAdvertisementDto;
import com.ori.domain.entity.Advertisement;
import com.ori.domain.vo.AdvertisementDetailVo;
import com.ori.domain.vo.AdvertisementListVo;

import java.util.List;


/**
 * 广告表(Advertisement)表服务接口
 *
 * @author leeway
 * @since 2025-04-05 01:13:55
 */
public interface AdvertisementService extends IService<Advertisement> {

    /**
     * 查询所有广告
     *
     * @return 所有广告数据
     */
    List<AdvertisementListVo> advertisementList();


    /**
     * 根据广告ID查询广告详情
     *
     * @param id 广告ID
     * @return 广告明细
     */
    AdvertisementDetailVo advertisementDetail(Long id);

    /**
     * 新增广告
     *
     * @param addAdvertisementDto 要新增广告数据
     */
    void addAdvertisement(AddAdvertisementDto addAdvertisementDto);

    /**
     * 修改广告
     *
     * @param updateAdvertisementDto 要修改的广告数据
     */
    void updateAdvertisement(UpdateAdvertisementDto updateAdvertisementDto);

    /**
     * 删除广告
     *
     * @param ids 广告ID集合
     */
    void deleteAdvertisement(List<Long> ids);
}
