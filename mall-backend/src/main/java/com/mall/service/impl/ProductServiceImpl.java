package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Product;
import com.mall.mapper.ProductMapper;
import com.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 商品服务实现类
 * 
 * @author mall
 * @date 2024-01-01
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ProductMapper productMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${file.upload.path:./uploads}")
    private String uploadPath;

    @Value("${file.upload.url:/uploads}")
    private String uploadUrl;

    /** Redis缓存前缀 */
    private static final String REDIS_KEY_PREFIX = "product:";
    private static final String REDIS_KEY_LIST = "product:list";
    
    /** Redis缓存过期时间（分钟） */
    private static final long REDIS_EXPIRE_TIME = 30;

    /**
     * 根据ID查询商品
     */
    @Override
    public Result<Product> getProductById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        
        Product product = (Product) redisTemplate.opsForValue().get(redisKey);
        if (product == null) {
            product = productMapper.selectById(id);
            if (product != null) {
                redisTemplate.opsForValue().set(redisKey, product, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        
        return Result.success(product);
    }

    /**
     * 查询商品列表
     */
    @Override
    public Result<List<Product>> getProductList(String name, Long categoryId, Integer status) {
        List<Product> productList = productMapper.selectList(name, categoryId, status);
        return Result.success(productList);
    }

    /**
     * 新增商品
     */
    @Override
    public Result<String> addProduct(Product product) {
        int result = productMapper.insert(product);
        if (result > 0) {
            clearProductListCache();
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新商品
     */
    @Override
    public Result<String> updateProduct(Product product) {
        int result = productMapper.update(product);
        if (result > 0) {
            clearProductCache(product.getId());
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 更新商品状态
     */
    @Override
    public Result<String> updateProductStatus(Long id, Integer status) {
        int result = productMapper.updateStatus(id, status);
        if (result > 0) {
            clearProductCache(id);
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }

    /**
     * 删除商品
     */
    @Override
    public Result<String> deleteProduct(Long id) {
        int result = productMapper.deleteById(id);
        if (result > 0) {
            clearProductCache(id);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 上传商品图片
     */
    @Override
    public Result<String> uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }
        
        // 获取文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // 生成新文件名
        String newFilename = UUID.randomUUID().toString() + suffix;
        
        // 创建上传目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 保存文件
        File destFile = new File(uploadDir, newFilename);
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            return Result.error("文件上传失败：" + e.getMessage());
        }
        
        // 返回文件访问URL
        String fileUrl = uploadUrl + "/" + newFilename;
        return Result.success(fileUrl);
    }

    /**
     * 清除商品缓存
     */
    private void clearProductCache(Long productId) {
        redisTemplate.delete(REDIS_KEY_PREFIX + productId);
        clearProductListCache();
    }

    /**
     * 清除商品列表缓存
     */
    private void clearProductListCache() {
        redisTemplate.delete(REDIS_KEY_LIST);
    }
}
