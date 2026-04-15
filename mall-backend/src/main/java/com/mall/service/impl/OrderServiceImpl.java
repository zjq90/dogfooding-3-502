package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.entity.OrderItem;
import com.mall.mapper.OrderItemMapper;
import com.mall.mapper.OrderMapper;
import com.mall.service.OrderService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 订单服务实现类
 * 
 * @author mall
 * @date 2024-01-01
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /** Redis缓存前缀 */
    private static final String REDIS_KEY_PREFIX = "order:";
    
    /** Redis缓存过期时间（分钟） */
    private static final long REDIS_EXPIRE_TIME = 30;

    /**
     * 根据ID查询订单
     */
    @Override
    public Result<Order> getOrderById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        
        Order order = (Order) redisTemplate.opsForValue().get(redisKey);
        if (order == null) {
            order = orderMapper.selectById(id);
            if (order != null) {
                // 查询订单明细
                List<OrderItem> orderItems = orderItemMapper.selectByOrderId(id);
                order.setOrderItems(orderItems);
                
                redisTemplate.opsForValue().set(redisKey, order, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        
        return Result.success(order);
    }

    /**
     * 查询订单列表
     */
    @Override
    public Result<List<Order>> getOrderList(String orderNo, String username, Integer orderStatus) {
        List<Order> orderList = orderMapper.selectList(orderNo, username, orderStatus);
        return Result.success(orderList);
    }

    /**
     * 更新订单
     */
    @Override
    public Result<String> updateOrder(Order order) {
        int result = orderMapper.update(order);
        if (result > 0) {
            clearOrderCache(order.getId());
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 关闭订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> closeOrder(Long id) {
        // 订单状态：5-已关闭
        int result = orderMapper.updateStatus(id, 5);
        if (result > 0) {
            clearOrderCache(id);
            return Result.success("订单关闭成功");
        }
        return Result.error("订单关闭失败");
    }

    /**
     * 删除订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> deleteOrder(Long id) {
        // 先删除订单明细
        orderItemMapper.deleteByOrderId(id);
        
        // 再删除订单
        int result = orderMapper.deleteById(id);
        if (result > 0) {
            clearOrderCache(id);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 清除订单缓存
     */
    private void clearOrderCache(Long orderId) {
        redisTemplate.delete(REDIS_KEY_PREFIX + orderId);
    }
}
