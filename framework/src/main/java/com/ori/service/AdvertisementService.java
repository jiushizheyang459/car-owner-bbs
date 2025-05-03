package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddAdvertisementDto;
import com.ori.domain.dto.UpdateAdvertisementDto;
import com.ori.domain.entity.Advertisement;
import com.ori.domain.vo.AdvertisementDetailVo;
import com.ori.domain.vo.AdvertisementListVo;
import com.ori.domain.vo.PageVo;

import java.util.List;


/**
 * 广告表(Advertisement)表服务接口
 *
 * @author leeway
 * @since 2025-04-05 01:13:55
 */
public interface AdvertisementService extends IService<Advertisement> {

    /**
     * 分页查询所有广告
     * 按创建时间降序排列
     * 只查询未删除的
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 分页查询结果
     */
    PageVo advertisementList(Integer pageNum, Integer pageSize);

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
    PageVo displayAdvertisementList(Integer pageNum, Integer pageSize);

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
