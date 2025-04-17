package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddInformationDto;
import com.ori.domain.dto.UpdateInformationDto;
import com.ori.domain.vo.InformationDetailVo;
import com.ori.domain.vo.informationListVo;
import com.ori.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资讯表(Information)表控制层
 *
 * @author leeway
 * @since 2025-04-15 00:02:17
 */
@RestController
@RequestMapping("information")
public class InformationController {
    /**
     * 服务对象
     */
    @Autowired
    private InformationService informationService;

    /**
     * 查询所有资讯
     *
     * @return 所有资讯
     */
    @GetMapping
    public ResponseResult informationList() {
        List<informationListVo> vos = informationService.informationList();
        return ResponseResult.okResult(vos);
    }

    /**
     * 根据资讯ID查询资讯详情
     *
     * @param id 资讯ID
     * @return 资讯明细
     */
    @GetMapping("{id}")
    public ResponseResult informationDetail(@PathVariable("id") Long id) {
        InformationDetailVo vo = informationService.informationDetail(id);
        return ResponseResult.okResult(vo);
    }

    /**
     * 新增资讯
     *
     * @param addInformationDto 要新增资讯数据
     * @return 新增结果
     */
    @PostMapping
    public ResponseResult addInformation(@RequestBody AddInformationDto addInformationDto) {
        informationService.addInformation(addInformationDto);
        return ResponseResult.okResult();
    }

    /**
     * 修改资讯
     *
     * @param updateInformationDto 要修改的资讯数据
     * @return 修改结果
     */
    @PutMapping
    public ResponseResult updateInformation(@RequestBody UpdateInformationDto updateInformationDto) {
        informationService.updateInformation(updateInformationDto);
        return ResponseResult.okResult();
    }

    /**
     * 删除资讯
     *
     * @param ids 资讯ID集合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteInformation(@RequestParam List<Long> ids) {
        informationService.deleteInformation(ids);
        return ResponseResult.okResult();
    }
}

