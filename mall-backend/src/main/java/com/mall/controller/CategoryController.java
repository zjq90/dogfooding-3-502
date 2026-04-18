package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Category;
import com.mall.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品分类控制器
 * 处理商品分类管理相关请求
 * 
 * @author mall
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 根据ID查询分类
     * 
     * @param id 分类ID
     * @return 分类信息
     */
    @GetMapping("/{id}")
    public Result<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    /**
     * 查询所有分类
     * 
     * @return 分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * 查询启用的分类列表
     * 
     * @return 分类列表
     */
    @GetMapping("/active")
    public Result<List<Category>> getActiveCategories() {
        return categoryService.getActiveCategories();
    }

    /**
     * 新增分类
     * 
     * @param category 分类信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    /**
     * 更新分类
     * 
     * @param category 分类信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    /**
     * 删除分类
     * 
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}
