package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Category;
import com.mall.entity.Product;
import com.mall.service.CategoryService;
import com.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 商品管理控制器
 * 处理商品的增删改查和分类管理
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取所有商品分类
     * @return 分类列表
     */
    @GetMapping("/categories")
    public Result<List<Category>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * 获取商品列表
     * @param name 商品名称（模糊查询）
     * @param categoryId 分类ID
     * @param status 商品状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 商品列表（带分页）
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getProductList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return productService.getProductList(name, categoryId, status, page, pageSize);
    }

    /**
     * 根据ID获取商品详情
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/{id}")
    public Result<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * 新增商品
     * @param product 商品信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    /**
     * 更新商品信息
     * @param product 商品信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateProduct(@RequestBody Product product) {
        return productService.updateProduct(product);
    }

    /**
     * 更新商品状态（上架/下架）
     * @param id 商品ID
     * @param status 商品状态
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<String> updateProductStatus(@PathVariable Long id, @RequestParam Integer status) {
        return productService.updateProductStatus(id, status);
    }

    /**
     * 删除商品
     * @param id 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    /**
     * 商品图片上传
     * @param file 图片文件
     * @return 图片访问路径
     */
    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        return productService.uploadImage(file);
    }
}
