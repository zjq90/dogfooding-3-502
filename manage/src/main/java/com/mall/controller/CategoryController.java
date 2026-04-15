package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Category;
import com.mall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> getCategoryList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer status) {
        return categoryService.getCategoryList(name, status);
    }

    @GetMapping("/all")
    public Result<List<Category>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Result<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public Result<String> addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category) {
        return categoryService.updateCategory(category);
    }

    @PutMapping("/{id}/status")
    public Result<String> updateCategoryStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return categoryService.updateCategoryStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        return categoryService.deleteCategory(id);
    }
}
