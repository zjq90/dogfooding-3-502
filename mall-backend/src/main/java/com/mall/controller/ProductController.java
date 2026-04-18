package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Product;
import com.mall.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品控制器
 * 处理商品管理相关请求
 * 
 * @author mall
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Resource
    private ProductService productService;

    /**
     * 根据ID查询商品
     * 
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/{id}")
    public Result<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * 查询商品列表
     * 
     * @param name 商品名称（模糊查询）
     * @param categoryId 分类ID
     * @param status 状态
     * @return 商品列表
     */
    @GetMapping("/list")
    public Result<List<Product>> getProductList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status) {
        return productService.getProductList(name, categoryId, status);
    }

    /**
     * 新增商品
     * 
     * @param product 商品信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    /**
     * 更新商品
     * 
     * @param product 商品信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    /**
     * 更新商品状态
     * 
     * @param id 商品ID
     * @param status 状态
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<String> updateProductStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return productService.updateProductStatus(id, status);
    }

    /**
     * 删除商品
     * 
     * @param id 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    /**
     * 上传商品图片
     * 
     * @param file 图片文件
     * @return 图片URL
     */
    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return productService.uploadImage(file);
    }
}
