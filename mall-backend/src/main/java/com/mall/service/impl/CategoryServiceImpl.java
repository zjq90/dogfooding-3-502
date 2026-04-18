package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Category;
import com.mall.mapper.CategoryMapper;
import com.mall.service.CategoryService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 商品分类服务实现类
 * 
 * @author mall
 * @date 2024-01-01
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /** Redis缓存Key */
    private static final String REDIS_KEY_ALL = "category:all";
    private static final String REDIS_KEY_ACTIVE = "category:active";
    private static final String REDIS_KEY_PREFIX = "category:";
    
    /** Redis缓存过期时间（分钟） */
    private static final long REDIS_EXPIRE_TIME = 30;

    /**
     * 根据ID查询分类
     */
    @Override
    public Result<Category> getCategoryById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        
        Category category = (Category) redisTemplate.opsForValue().get(redisKey);
        if (category == null) {
            category = categoryMapper.selectById(id);
            if (category != null) {
                redisTemplate.opsForValue().set(redisKey, category, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        
        return Result.success(category);
    }

    /**
     * 查询所有分类
     */
    @Override
    @SuppressWarnings("unchecked")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = (List<Category>) redisTemplate.opsForValue().get(REDIS_KEY_ALL);
        if (categories == null) {
            categories = categoryMapper.selectAll();
            redisTemplate.opsForValue().set(REDIS_KEY_ALL, categories, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
        }
        return Result.success(categories);
    }

    /**
     * 查询启用的分类列表
     */
    @Override
    @SuppressWarnings("unchecked")
    public Result<List<Category>> getActiveCategories() {
        List<Category> categories = (List<Category>) redisTemplate.opsForValue().get(REDIS_KEY_ACTIVE);
        if (categories == null) {
            categories = categoryMapper.selectActiveList();
            redisTemplate.opsForValue().set(REDIS_KEY_ACTIVE, categories, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
        }
        return Result.success(categories);
    }

    /**
     * 新增分类
     */
    @Override
    public Result<String> addCategory(Category category) {
        int result = categoryMapper.insert(category);
        if (result > 0) {
            clearCategoryCache();
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新分类
     */
    @Override
    public Result<String> updateCategory(Category category) {
        int result = categoryMapper.update(category);
        if (result > 0) {
            clearCategoryCache();
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除分类
     */
    @Override
    public Result<String> deleteCategory(Long id) {
        int result = categoryMapper.deleteById(id);
        if (result > 0) {
            clearCategoryCache();
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 清除分类缓存
     */
    private void clearCategoryCache() {
        redisTemplate.delete(REDIS_KEY_ALL);
        redisTemplate.delete(REDIS_KEY_ACTIVE);
    }
}
