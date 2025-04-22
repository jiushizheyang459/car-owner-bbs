package com.ori.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.Follows;
import com.ori.domain.entity.Save;
import com.ori.domain.vo.ArticleDetailVo;
import com.ori.domain.vo.PageVo;
import com.ori.domain.vo.SaveListVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.SaveMapper;
import com.ori.service.ArticleService;
import com.ori.service.SaveService;
import com.ori.utils.SecurityUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 收藏表(Save)表服务实现类
 *
 * @author leeway
 * @since 2025-04-18 00:45:35
 */
@Service("saveService")
public class SaveServiceImpl extends ServiceImpl<SaveMapper, Save> implements SaveService {

    @Autowired
    private ArticleService articleService;

    @Override
    public PageVo saveList(Integer pageNum, Integer size) {
        Long userId = SecurityUtils.getUserId();

        Page<Save> page = lambdaQuery()
                .eq(Save::getUserId, userId)
                .eq(Save::getDelFlag, 0)
                .orderByDesc(Save::getCreateTime)
                .page(new Page<>(pageNum, size));

        List<SaveListVo> vos = convertToVoList(page.getRecords());

        return new PageVo(vos, page.getTotal());
    }

    private List<SaveListVo> convertToVoList(List<Save> saves) {
        if (CollectionUtils.isEmpty(saves)) {
            return Collections.emptyList();
        }

        List<Long> articleIds = saves.stream()
                .map(Save::getArticleId)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, ArticleDetailVo> articleMap = articleService.getArticleDetailMap(articleIds);

        return saves.stream().map(save -> {
            ArticleDetailVo detail = articleMap.get(save.getArticleId());
            SaveListVo vo = new SaveListVo();
            vo.setId(save.getId());
            vo.setArticleTitle(detail != null ? detail.getTitle() : "");
            vo.setArticleContent(detail != null ? detail.getContent() : "");
            vo.setCreateTime(save.getCreateTime());
            return vo;
        }).collect(Collectors.toList());
    }


    @Override
    public void addSave(Long articleId) {
        Long userId = SecurityUtils.getUserId();

        Save existingSave = getBaseMapper().selectExisting(userId, articleId);

        if (existingSave != null) {
            if (existingSave.getDelFlag() == 0) {
                // 已收藏则取消
                deleteSave(articleId);
                return;
            } else {
                // 曾收藏过，恢复收藏
                getBaseMapper().reSave(existingSave.getId());
                return;
            }
        }

        // 从未收藏过，插入新记录
        Save save = new Save();
        save.setUserId(userId);
        save.setArticleId(articleId);
        save(save);
    }


    @Override
    public void deleteSave(Long articleId) {
        Long userId = SecurityUtils.getUserId();

        // 直接更新 `del_flag`，如果受影响行数为 0，说明本来就没收藏
        boolean updated = lambdaUpdate()
                .set(Save::getDelFlag, 1)
                .eq(Save::getUserId, userId)
                .eq(Save::getArticleId, articleId)
                .eq(Save::getDelFlag, 0) // 只更新已收藏
                .update();

        if (!updated) {
            throw new SystemException(AppHttpCodeEnum.SAVE_EXIST);
        }
    }

    @Override
    public boolean isArticleSaved(Long articleId) {
        Long userId = SecurityUtils.getUserId();
        
        // 查询用户是否已收藏该文章（未删除的记录）
        return lambdaQuery()
                .eq(Save::getUserId, userId)
                .eq(Save::getArticleId, articleId)
                .eq(Save::getDelFlag, 0)
                .count() > 0;
    }
}
