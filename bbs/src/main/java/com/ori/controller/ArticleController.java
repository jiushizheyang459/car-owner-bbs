package com.ori.controller;


import com.ori.annotation.SystemLog;
import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddArticleDto;
import com.ori.domain.dto.AddEventDto;
import com.ori.domain.dto.UpdateArticleDto;
import com.ori.domain.dto.UpdateEventDto;
import com.ori.domain.vo.ArticleDetailVo;
import com.ori.domain.vo.HotArticleVo;
import com.ori.domain.vo.NewArticleVo;
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
     * @return 4篇热门文章
     */
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        List<HotArticleVo> vos = articleService.hotArticleList();
        return ResponseResult.okResult(vos);
    }

    /**
     * 查询最新文章
     *
     * @return 8篇最新文章
     */
    @GetMapping("/newArticleList")
    public ResponseResult newArticleList() {
        List<NewArticleVo> vos = articleService.newArticleList();
        return ResponseResult.okResult(vos);
    }

    /**
     * 分页查询全部文章
     *
     * @param pageNum 多少页
     * @param size 每页多少条
     * @return 全部文章结果
     */
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer size) {
        PageVo vo = articleService.articleList(pageNum, size);
        return ResponseResult.okResult(vo);
    }

    /**
     * 分页查询当前用户关注的用户发表的文章
     *
     * @param pageNum 多少页
     * @param size 每页多少条
     * @return 当前用户关注的用户发表的文章
     */
    @GetMapping("/follow")
    public ResponseResult FollowArticleList(Integer pageNum, Integer size) {
        PageVo vo = articleService.FollowArticleList(pageNum, size);
        return ResponseResult.okResult(vo);
    }

    /**
     * 分页查询当前用户的草稿文章
     *
     * @param pageNum 多少页
     * @param size 每页多少条
     * @return 当前用户的草稿文章
     */
    @GetMapping("/draftArticleList")
    public ResponseResult draftArticleList(Integer pageNum, Integer size) {
        PageVo vo = articleService.draftArticleList(pageNum, size);
        return ResponseResult.okResult(vo);
    }

    /**
     * 根据文章ID查询文章详情
     *
     * @param id 文章ID
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

    /**
     * 新增文章
     *
     * @param addArticleDto 要新增文章数据
     * @return 新增结果
     */
    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto addArticleDto) {
        articleService.addArticle(addArticleDto);
        return ResponseResult.okResult();
    }

    /**
     * 新增草稿文章
     *
     * @param addArticleDto 要新增文章数据
     * @return 新增结果
     */
    @PostMapping("/addDraftArticle")
    public ResponseResult addDraftArticle(@RequestBody AddArticleDto addArticleDto) {
        articleService.addDraftArticle(addArticleDto);
        return ResponseResult.okResult();
    }

    /**
     * 修改文章
     *
     * @param updateArticleDto 要修改的文章数据
     * @return 修改结果
     */
    @PutMapping
    public ResponseResult updateArticle(@RequestBody UpdateArticleDto updateArticleDto) {
        articleService.updateArticle(updateArticleDto);
        return ResponseResult.okResult();
    }

    /**
     * 删除文章
     *
     * @param ids 文章ID集合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteArticle(@RequestParam List<Long> ids) {
        articleService.deleteArticle(ids);
        return ResponseResult.okResult();
    }
}


