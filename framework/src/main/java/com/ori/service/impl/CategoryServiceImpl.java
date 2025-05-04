package com.ori.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ori.constants.SystemConstants;
import com.ori.domain.dto.AddCategoryDto;
import com.ori.domain.dto.UpdateCategoryDto;
import com.ori.domain.entity.Article;
import com.ori.domain.entity.Category;
import com.ori.domain.entity.Event;
import com.ori.domain.vo.CategoryVo;
import com.ori.enums.AppHttpCodeEnum;
import com.ori.exception.SystemException;
import com.ori.mapper.CategoryMapper;
import com.ori.service.ArticleService;
import com.ori.service.CategoryService;
import com.ori.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    
    @Autowired
    private ArticleService articleService;

    /**
     * 查询所有车型分类
     *
     * @return 返回所有车型分类
     */
    @Override
    public List<CategoryVo> categoryList() {
        List<Article> articleList = articleService.lambdaQuery()
                .eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .list();

        Set<Long> categoryIds = articleList.stream()
                .map(Article::getCategoryId)
                .collect(Collectors.toSet());
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream()
                .filter(category -> category.getStatus() == SystemConstants.CATEGORY_STATUS_NORMAL)
                .collect(Collectors.toList());

        List<CategoryVo> vos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);

        return vos;
    }

    @Transactional
    @Override
    public void addCategory(AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);

        if (!StringUtils.hasText(category.getName())) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_NULL);
        }
        save(category);
    }

    @Transactional
    @Override
    public void updateCategory(UpdateCategoryDto updateCategoryDto) {
        Category category = getById(updateCategoryDto.getId());
        if (Objects.isNull(category)) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_NOT_FOUND);
        }
        category = BeanCopyUtils.copyBean(updateCategoryDto, Category.class);
        updateById(category);
    }

    @Transactional
    @Override
    public void deleteCategory(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new SystemException(AppHttpCodeEnum.CATEGORY_IDS_NOT_NULL);
        }
        lambdaUpdate()
                .set(Category::getDelFlag, 1)
                .in(Category::getId, ids);
    }
}
