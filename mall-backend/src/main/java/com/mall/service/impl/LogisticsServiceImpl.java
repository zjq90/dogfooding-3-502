package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Logistics;
import com.mall.mapper.LogisticsMapper;
import com.mall.service.LogisticsService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 物流服务实现类
 * 
 * @author mall
 * @date 2024-01-01
 */
@Service
public class LogisticsServiceImpl implements LogisticsService {

    @Resource
    private LogisticsMapper logisticsMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /** Redis缓存前缀 */
    private static final String REDIS_KEY_PREFIX = "logistics:";
    private static final String REDIS_KEY_ORDER_PREFIX = "logistics:order:";
    
    /** Redis缓存过期时间（分钟） */
    private static final long REDIS_EXPIRE_TIME = 30;

    /**
     * 根据ID查询物流信息
     */
    @Override
    public Result<Logistics> getLogisticsById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        
        Logistics logistics = (Logistics) redisTemplate.opsForValue().get(redisKey);
        if (logistics == null) {
            logistics = logisticsMapper.selectById(id);
            if (logistics != null) {
                redisTemplate.opsForValue().set(redisKey, logistics, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        
        return Result.success(logistics);
    }

    /**
     * 根据订单ID查询物流信息
     */
    @Override
    public Result<Logistics> getLogisticsByOrderId(Long orderId) {
        String redisKey = REDIS_KEY_ORDER_PREFIX + orderId;
        
        Logistics logistics = (Logistics) redisTemplate.opsForValue().get(redisKey);
        if (logistics == null) {
            logistics = logisticsMapper.selectByOrderId(orderId);
            if (logistics != null) {
                redisTemplate.opsForValue().set(redisKey, logistics, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        
        return Result.success(logistics);
    }

    /**
     * 查询物流列表
     */
    @Override
    public Result<List<Logistics>> getLogisticsList(String orderNo, Integer shippingStatus) {
        List<Logistics> logisticsList = logisticsMapper.selectList(orderNo, shippingStatus);
        return Result.success(logisticsList);
    }

    /**
     * 新增物流信息
     */
    @Override
    public Result<String> addLogistics(Logistics logistics) {
        int result = logisticsMapper.insert(logistics);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新物流信息
     */
    @Override
    public Result<String> updateLogistics(Logistics logistics) {
        int result = logisticsMapper.update(logistics);
        if (result > 0) {
            clearLogisticsCache(logistics.getId());
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除物流信息
     */
    @Override
    public Result<String> deleteLogistics(Long id) {
        Logistics logistics = logisticsMapper.selectById(id);
        int result = logisticsMapper.deleteById(id);
        if (result > 0) {
            clearLogisticsCache(id);
            if (logistics != null) {
                redisTemplate.delete(REDIS_KEY_ORDER_PREFIX + logistics.getOrderId());
            }
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 清除物流缓存
     */
    private void clearLogisticsCache(Long logisticsId) {
        redisTemplate.delete(REDIS_KEY_PREFIX + logisticsId);
    }
}
