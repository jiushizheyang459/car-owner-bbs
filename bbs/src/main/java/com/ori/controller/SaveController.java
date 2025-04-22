package com.ori.controller;


import com.ori.domain.ResponseResult;
import com.ori.domain.vo.PageVo;
import com.ori.service.SaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 收藏表(Save)表控制层
 *
 * @author leeway
 * @since 2025-04-18 00:45:35
 */
@RestController
@RequestMapping("/save")
public class SaveController {
    /**
     * 服务对象
     */
    @Autowired
    private SaveService saveService;

    /**
     * 分页查询全部收藏
     *
     * @param pageNum 多少页
     * @param size 每页多少条
     * @return 全部收藏结果
     */
    @GetMapping("/saveList")
    public ResponseResult saveList(Integer pageNum, Integer size) {
        PageVo vo = saveService.saveList(pageNum, size);
        return ResponseResult.okResult(vo);
    }

    /**
     * 新增收藏
     *
     * @param articleId 要收藏的文章ID
     * @return 新增结果
     */
    @PostMapping
    public ResponseResult addSave(Long articleId) {
        saveService.addSave(articleId);
        return ResponseResult.okResult();
    }

    /**
     * 取消收藏
     *
     * @param articleId 要取消收藏的文章ID
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteSave(Long articleId) {
        saveService.deleteSave(articleId);
        return ResponseResult.okResult();
    }

    /**
     * 检查当前用户是否已收藏指定文章
     *
     * @param articleId 文章ID
     * @return 是否已收藏
     */
    @GetMapping("/status/{articleId}")
    public ResponseResult isArticleSaved(@PathVariable("articleId") Long articleId) {
        boolean isSaved = saveService.isArticleSaved(articleId);
        return ResponseResult.okResult(isSaved);
    }
}

