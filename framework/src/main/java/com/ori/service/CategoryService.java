package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.entity.Category;
import com.ori.domain.vo.CategoryVo;

import java.util.List;

public interface CategoryService extends IService<Category> {

    /**
     * 查询所有车型分类
     *
     * @return 所有车型分类
     */
    List<CategoryVo> categoryList();
}
