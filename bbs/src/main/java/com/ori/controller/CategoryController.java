package com.ori.controller;

import com.ori.domain.ResponseResult;
import com.ori.domain.vo.CategoryVo;
import com.ori.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
