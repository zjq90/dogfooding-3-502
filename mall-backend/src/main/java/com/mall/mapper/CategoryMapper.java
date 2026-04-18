package com.mall.mapper;

import com.mall.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品分类数据访问层
 * 
 * @author mall
 * @date 2024-01-01
 */
@Mapper
public interface CategoryMapper {

    /**
     * 根据ID查询分类
     * 
     * @param id 分类ID
     * @return 分类信息
     */
    Category selectById(@Param("id") Long id);

    /**
     * 查询所有分类
     * 
     * @return 分类列表
     */
    List<Category> selectAll();

    /**
     * 查询启用的分类列表
     * 
     * @return 分类列表
     */
    List<Category> selectActiveList();

    /**
     * 新增分类
     * 
     * @param category 分类信息
     * @return 影响行数
     */
    int insert(Category category);

    /**
     * 更新分类
     * 
     * @param category 分类信息
     * @return 影响行数
     */
    int update(Category category);

    /**
     * 删除分类
     * 
     * @param id 分类ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
