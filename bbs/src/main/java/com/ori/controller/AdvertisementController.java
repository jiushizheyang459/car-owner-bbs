package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddAdvertisementDto;
import com.ori.domain.dto.UpdateAdvertisementDto;
import com.ori.domain.vo.AdvertisementListVo;
import com.ori.domain.vo.AdvertisementDetailVo;
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
     * 查询所有广告
     *
     * @return 所有广告数据
     */
    @GetMapping
    public ResponseResult advertisementList() {
        List<AdvertisementListVo> vos =  advertisementService.advertisementList();
        return ResponseResult.okResult(vos);
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

