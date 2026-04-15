package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商品服务接口
 * 
 * @author mall
 * @date 2024-01-01
 */
public interface ProductService {

    /**
     * 根据ID查询商品
     * 
     * @param id 商品ID
     * @return 商品信息
     */
    Result<Product> getProductById(Long id);

    /**
     * 查询商品列表
     * 
     * @param name 商品名称（模糊查询）
     * @param categoryId 分类ID
     * @param status 状态
     * @return 商品列表
     */
    Result<List<Product>> getProductList(String name, Long categoryId, Integer status);

    /**
     * 新增商品
     * 
     * @param product 商品信息
     * @return 操作结果
     */
    Result<String> addProduct(Product product);

    /**
     * 更新商品
     * 
     * @param product 商品信息
     * @return 操作结果
     */
    Result<String> updateProduct(Product product);

    /**
     * 更新商品状态
     * 
     * @param id 商品ID
     * @param status 状态
     * @return 操作结果
     */
    Result<String> updateProductStatus(Long id, Integer status);

    /**
     * 删除商品
     * 
     * @param id 商品ID
     * @return 操作结果
     */
    Result<String> deleteProduct(Long id);

    /**
     * 上传商品图片
     * 
     * @param file 图片文件
     * @return 图片URL
     */
    Result<String> uploadImage(MultipartFile file);
}
