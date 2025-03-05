package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Article;
import com.ori.domain.vo.ArticleDetailVo;
import com.ori.domain.vo.HotArticleVo;
import com.ori.domain.vo.PageVo;

import java.util.List;

public interface ArticleService extends IService<Article> {
    /**
     * 查询热门文章
     *
     * @return 10篇热门文章
     */
    List<HotArticleVo> hotArticleList();

    /**
     * 分页查询文章
     *
     * @param pageNum
     * @param pageSize
     * @return 分页查询文章结果
     */
    PageVo articleList(Integer pageNum, Integer pageSize);

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
}
