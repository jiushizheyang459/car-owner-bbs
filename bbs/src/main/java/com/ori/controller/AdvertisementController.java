package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddAdvertisementDto;
import com.ori.domain.dto.UpdateAdvertisementDto;
import com.ori.domain.vo.AdvertisementListVo;
import com.ori.domain.vo.AdvertisementDetailVo;
import com.ori.domain.vo.PageVo;
import com.ori.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 广告表(Advertisement)表控制层
 *
 * @author leeway
 * @since 2025-04-05 01:13:55
 */
@RestController
@RequestMapping("/advertisement")
public class AdvertisementController {
    /**
     * 服务对象
     */
    @Autowired
    private AdvertisementService advertisementService;

    /**
     * 分页查询所有广告
     * 按创建时间降序排列
     * 只查询未删除的
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 分页查询结果
     */
    @GetMapping("/advertisementList")
    public ResponseResult advertisementList(Integer pageNum, Integer pageSize) {
        PageVo vo =  advertisementService.advertisementList(pageNum, pageSize);
        return ResponseResult.okResult(vo);
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
    @GetMapping("/displayAdvertisementList")
    public ResponseResult displayAdvertisementList(Integer pageNum, Integer pageSize) {
        PageVo vo =  advertisementService.displayAdvertisementList(pageNum, pageSize);
        return ResponseResult.okResult(vo);
    }

    /**
     * 根据广告ID查询广告详情
     *
     * @param id 广告ID
     * @return 广告明细
     */
    @GetMapping("{id}")
    public ResponseResult advertisementDetail(@PathVariable("id") Long id) {
        AdvertisementDetailVo vo = advertisementService.advertisementDetail(id);
        return ResponseResult.okResult(vo);
    }

    /**
     * 新增广告
     *
     * @param addAdvertisementDto 要新增广告数据
     * @return 新增结果
     */
    @PostMapping
    public ResponseResult addAdvertisement(@RequestBody AddAdvertisementDto addAdvertisementDto) {
        advertisementService.addAdvertisement(addAdvertisementDto);
        return ResponseResult.okResult();
    }

    /**
     * 修改广告
     *
     * @param updateAdvertisementDto 要修改的广告数据
     * @return 修改结果
     */
    @PutMapping
    public ResponseResult updateAdvertisement(@RequestBody UpdateAdvertisementDto updateAdvertisementDto) {
        advertisementService.updateAdvertisement(updateAdvertisementDto);
        return ResponseResult.okResult();
    }

    /**
     * 删除广告
     *
     * @param ids 广告ID集合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteAdvertisement(@RequestParam List<Long> ids) {
        advertisementService.deleteAdvertisement(ids);
        return ResponseResult.okResult();
    }
}

