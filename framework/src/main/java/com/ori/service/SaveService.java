package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Save;
import com.ori.domain.vo.PageVo;


/**
 * 收藏表(Save)表服务接口
 *
 * @author leeway
 * @since 2025-04-18 00:45:35
 */
public interface SaveService extends IService<Save> {

    /**
     * 分页查询全部收藏
     *
     * @param pageNum 多少页
     * @param size 每页多少条
     * @return 全部收藏结果
     */
    PageVo saveList(Integer pageNum, Integer size);

    /**
     * 新增收藏
     *
     * @param articleId 要收藏的文章ID
     */
    void addSave(Long articleId);

    /**
     * 取消收藏
     *
     * @param articleId 要取消收藏的文章ID
     */
    void deleteSave(Long articleId);
}
