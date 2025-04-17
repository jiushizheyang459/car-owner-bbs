package com.ori.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ori.domain.dto.AddCategoryDto;
import com.ori.domain.dto.UpdateCategoryDto;
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

    /**
     * 新增车辆分类
     *
     * @param addCategoryDto 要新增车辆分类数据
     */
    void addCategory(AddCategoryDto addCategoryDto);

    /**
     * 修改车辆分类
     *
     * @param updateCategoryDto 要修改的车辆分类数据
     */
    void updateCategory(UpdateCategoryDto updateCategoryDto);

    /**
     * 删除车辆分类
     *
     * @param ids 车辆分类ID集合
     */
    void deleteCategory(List<Long> ids);
}
