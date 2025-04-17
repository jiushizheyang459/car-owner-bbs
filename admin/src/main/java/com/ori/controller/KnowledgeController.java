package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddKnowledgeDto;
import com.ori.domain.dto.UpdateKnowledgeDto;
import com.ori.domain.vo.KnowledgeDetailVo;
import com.ori.domain.vo.PageVo;
import com.ori.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 知识表(Knowledge)表控制层
 *
 * @author leeway
 * @since 2025-04-16 22:50:02
 */
@RestController
@RequestMapping("knowledge")
public class KnowledgeController {
    /**
     * 服务对象
     */
    @Autowired
    private KnowledgeService knowledgeService;

    /**
     * 分页查询全部知识
     *
     * @param pageNum 多少页
     * @param size 每页多少条
     * @return 全部知识结果
     */
    @GetMapping("/articleList")
    public ResponseResult knowledgeList(Integer pageNum, Integer size) {
        PageVo vo = knowledgeService.knowledgeList(pageNum, size);
        return ResponseResult.okResult(vo);
    }

    /**
     * 根据知识ID查询知识详情
     *
     * @param id 知识ID
     * @return 知识明细
     */
    @GetMapping("{id}")
    public ResponseResult knowledgeDetail(@PathVariable("id") Long id) {
        KnowledgeDetailVo vo = knowledgeService.knowledgeDetail(id);
        return ResponseResult.okResult(vo);
    }

    /**
     * 新增知识
     *
     * @param addKnowledgeDto 要新增知识数据
     * @return 新增结果
     */
    @PostMapping
    public ResponseResult addKnowledge(@RequestBody AddKnowledgeDto addKnowledgeDto) {
        knowledgeService.addKnowledge(addKnowledgeDto);
        return ResponseResult.okResult();
    }

    /**
     * 修改知识
     *
     * @param updateKnowledgeDto 要修改的知识数据
     * @return 修改结果
     */
    @PutMapping
    public ResponseResult updateKnowledge(@RequestBody UpdateKnowledgeDto updateKnowledgeDto) {
        knowledgeService.updateKnowledge(updateKnowledgeDto);
        return ResponseResult.okResult();
    }

    /**
     * 删除知识
     *
     * @param ids 知识ID集合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteKnowledge(@RequestParam List<Long> ids) {
        knowledgeService.deleteKnowledge(ids);
        return ResponseResult.okResult();
    }
}

