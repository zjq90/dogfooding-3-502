package com.mall.mapper;

import com.mall.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品数据访问层
 * 
 * @author mall
 * @date 2024-01-01
 */
@Mapper
public interface ProductMapper {

    /**
     * 根据ID查询商品
     * 
     * @param id 商品ID
     * @return 商品信息
     */
    Product selectById(@Param("id") Long id);

    /**
     * 查询商品列表
     * 
     * @param name 商品名称（模糊查询）
     * @param categoryId 分类ID
     * @param status 状态
     * @return 商品列表
     */
    List<Product> selectList(@Param("name") String name, 
                            @Param("categoryId") Long categoryId, 
                            @Param("status") Integer status);

    /**
     * 新增商品
     * 
     * @param product 商品信息
     * @return 影响行数
     */
    int insert(Product product);

    /**
     * 更新商品
     * 
     * @param product 商品信息
     * @return 影响行数
     */
    int update(Product product);

    /**
     * 更新商品状态
     * 
     * @param id 商品ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 删除商品
     * 
     * @param id 商品ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
