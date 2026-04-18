package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Category;

import java.util.List;

/**
 * 商品分类服务接口
 * 
 * @author mall
 * @date 2024-01-01
 */
public interface CategoryService {

    /**
     * 根据ID查询分类
     * 
     * @param id 分类ID
     * @return 分类信息
     */
    Result<Category> getCategoryById(Long id);

    /**
     * 查询所有分类
     * 
     * @return 分类列表
     */
    Result<List<Category>> getAllCategories();

    /**
     * 查询启用的分类列表
     * 
     * @return 分类列表
     */
    Result<List<Category>> getActiveCategories();

    /**
     * 新增分类
     * 
     * @param category 分类信息
     * @return 操作结果
     */
    Result<String> addCategory(Category category);

    /**
     * 更新分类
     * 
     * @param category 分类信息
     * @return 操作结果
     */
    Result<String> updateCategory(Category category);

    /**
     * 删除分类
     * 
     * @param id 分类ID
     * @return 操作结果
     */
    Result<String> deleteCategory(Long id);
}
