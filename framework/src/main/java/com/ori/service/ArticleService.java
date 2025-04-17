package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddArticleDto;
import com.ori.domain.dto.UpdateArticleDto;
import com.ori.domain.entity.Article;
import com.ori.domain.vo.ArticleDetailVo;
import com.ori.domain.vo.HotArticleVo;
import com.ori.domain.vo.NewArticleVo;
import com.ori.domain.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {
    /**
     * 查询热门文章
     *
     * @return 10篇热门文章
     */
    List<HotArticleVo> hotArticleList();

    /**
     * 查询最新文章
     *
     * @return 8篇最新文章
     */
    List<NewArticleVo> newArticleList();

    /**
     * 分页全部查询文章
     *
     * @param pageNum
     * @param pageSize
     * @return 全部文章结果
     */
    PageVo articleList(Integer pageNum, Integer pageSize);

    /**
     * 分页查询当前用户关注的用户发表的文章
     *
     * @param pageNum 多少页
     * @param pageSize 每页多少条
     * @return 当前用户关注的用户发表的文章
     */
    PageVo FollowArticleList(Integer pageNum, Integer pageSize);

    /**
     * 根据文章ID查询文章详情
     *
     * @param id
     * @return 文章明细
     */
    ArticleDetailVo articleDetail(Long id);

    /**
     * 更新文章浏览量
     * @param id 文章ID
     * @param userIdentifier 访问者标识
     */
    void updateViewCount(Long id, String userIdentifier);

    /**
     * 新增文章
     *
     * @param addArticleDto 要新增文章数据
     */
    void addArticle(AddArticleDto addArticleDto);

    /**
     * 修改文章
     *
     * @param updateArticleDto 要修改的文章数据
     */
    void updateArticle(UpdateArticleDto updateArticleDto);

    /**
     * 删除文章
     *
     * @param ids 文章ID集合
     */
    void deleteArticle(List<Long> ids);

    /**
     * 并发批量获取文章详情
     * 线程池 + CompletableFuture
     *
     * @param articleIds 文章ID集合
     * @return key为文章ID，value为文章明细的Map
     */
    Map<Long, ArticleDetailVo> getArticleDetailMap(List<Long> articleIds) ;
}
