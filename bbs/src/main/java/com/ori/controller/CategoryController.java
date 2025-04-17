package com.ori.controller;

import com.ori.domain.ResponseResult;
import com.ori.domain.dto.AddCategoryDto;
import com.ori.domain.dto.UpdateCategoryDto;
import com.ori.domain.vo.CategoryVo;
import com.ori.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询所有车型分类
     *
     * @return 所有车型分类
     */
    @GetMapping("/categoryList")
    public ResponseResult categoryList() {
        List<CategoryVo> vos = categoryService.categoryList();
        return ResponseResult.okResult(vos);
    }

    /**
     * 新增车辆分类
     *
     * @param addCategoryDto 要新增车辆分类数据
     * @return 新增结果
     */
    @PostMapping
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto) {
        categoryService.addCategory(addCategoryDto);
        return ResponseResult.okResult();
    }

    /**
     * 修改车辆分类
     *
     * @param updateCategoryDto 要修改的车辆分类数据
     * @return 修改结果
     */
    @PutMapping
    public ResponseResult updateCategory(@RequestBody UpdateCategoryDto updateCategoryDto) {
        categoryService.updateCategory(updateCategoryDto);
        return ResponseResult.okResult();
    }

    /**
     * 删除车辆分类
     *
     * @param ids 车辆分类ID集合
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseResult deleteCategory(@RequestParam List<Long> ids) {
        categoryService.deleteCategory(ids);
        return ResponseResult.okResult();
    }
    
}
