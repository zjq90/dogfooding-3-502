package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 商品服务接口
 * 定义商品相关的业务操作
 */
public interface ProductService {

    /**
     * 根据ID获取商品详情
     * @param id 商品ID
     * @return 商品信息
     */
    Result<Product> getProductById(Long id);

    /**
     * 获取商品列表（带分页）
     * @param name 商品名称
     * @param categoryId 分类ID
     * @param status 商品状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 商品列表和分页信息
     */
    Result<Map<String, Object>> getProductList(String name, Long categoryId, Integer status,
                                                Integer page, Integer pageSize);

    /**
     * 新增商品
     * @param product 商品信息
     * @return 操作结果
     */
    Result<String> addProduct(Product product);

    /**
     * 更新商品信息
     * @param product 商品信息
     * @return 操作结果
     */
    Result<String> updateProduct(Product product);

    /**
     * 更新商品状态
     * @param id 商品ID
     * @param status 商品状态
     * @return 操作结果
     */
    Result<String> updateProductStatus(Long id, Integer status);

    /**
     * 删除商品
     * @param id 商品ID
     * @return 操作结果
     */
    Result<String> deleteProduct(Long id);

    /**
     * 上传商品图片
     * @param file 图片文件
     * @return 图片访问路径
     */
    Result<String> uploadImage(MultipartFile file);
}
