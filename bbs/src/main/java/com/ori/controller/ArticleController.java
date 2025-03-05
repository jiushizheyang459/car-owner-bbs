package com.ori.controller;


import com.ori.annotation.SystemLog;
import com.ori.domain.ResponseResult;
import com.ori.domain.vo.ArticleDetailVo;
import com.ori.domain.vo.HotArticleVo;
import com.ori.domain.vo.PageVo;
import com.ori.service.ArticleService;
import com.ori.utils.IdentifierUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章表(Article)表控制层
 *
 * @author leeway
 * @since 2025-01-30 15:52:14
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 查询热门文章
     *
     * @return 10篇热门文章
     */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        List<HotArticleVo> vos = articleService.hotArticleList();
        return ResponseResult.okResult(vos);
    }

    /**
     * 分页查询文章
     *
     * @param pageNum
     * @param size
     * @return 分页查询文章结果
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer size) {
        PageVo vo = articleService.articleList(pageNum, size);
        return ResponseResult.okResult(vo);
    }

    /**
     * 根据文章ID查询文章详情
     *
     * @param id
     * @return 文章明细
     */
    @GetMapping("/{id}")
    public ResponseResult articleDetail(@PathVariable("id") Long id) {
        ArticleDetailVo vo = articleService.articleDetail(id);
        return ResponseResult.okResult(vo);
    }

    /**
     * 更新文章浏览量
     *
     * @param id 文章ID
     * @param request 请求
     * @return 响应成功
     */
    @PutMapping("/updateViewCount/{id}")
    @SystemLog(businessName = "更新文章浏览量")
    public ResponseResult updateViewCount(@PathVariable("id") Long id, HttpServletRequest request) {
        // 获取用户标识
        String userIdentifier = IdentifierUtils.getUserIdentifier(request);
        articleService.updateViewCount(id, userIdentifier);
        return ResponseResult.okResult();
    }
}


