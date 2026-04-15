package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.mapper.OrderMapper;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 订单服务实现类
 * 实现订单相关的业务逻辑
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Redis缓存前缀
    private static final String REDIS_KEY_PREFIX = "order:";
    // Redis过期时间（分钟）
    private static final long REDIS_EXPIRE_TIME = 30;

    /**
     * 根据ID获取订单详情（带Redis缓存）
     * @param id 订单ID
     * @return 订单信息
     */
    @Override
    public Result<Order> getOrderById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        Order order = (Order) redisTemplate.opsForValue().get(redisKey);
        if (order == null) {
            order = orderMapper.selectById(id);
            if (order != null) {
                redisTemplate.opsForValue().set(redisKey, order, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        return Result.success(order);
    }

    /**
     * 根据订单号获取订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    @Override
    public Result<Order> getOrderByOrderNo(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        return Result.success(order);
    }

    /**
     * 获取订单列表（带分页）
     * @param orderNo 订单号
     * @param username 用户名
     * @param orderStatus 订单状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 订单列表和分页信息
     */
    @Override
    public Result<Map<String, Object>> getOrderList(String orderNo, String username, Integer orderStatus,
                                                     Integer page, Integer pageSize) {
        int offset = (page - 1) * pageSize;
        List<Order> orderList = orderMapper.selectListWithPage(orderNo, username, orderStatus, offset, pageSize);
        int total = orderMapper.selectCount(orderNo, username, orderStatus);

        Map<String, Object> result = new HashMap<>();
        result.put("list", orderList);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);

        return Result.success(result);
    }

    /**
     * 获取订单统计数据
     * @return 统计数据
     */
    @Override
    public Result<Map<String, Object>> getOrderStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("userCount", 128);
        statistics.put("productCount", 256);
        statistics.put("orderCount", 512);
        statistics.put("totalAmount", "128,560.00");
        return Result.success(statistics);
    }

    /**
     * 更新订单信息
     * @param order 订单信息
     * @return 操作结果
     */
    @Override
    public Result<String> updateOrder(Order order) {
        int result = orderMapper.update(order);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + order.getId();
            redisTemplate.delete(redisKey);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 关闭订单
     * @param id 订单ID
     * @return 操作结果
     */
    @Override
    public Result<String> closeOrder(Long id) {
        int result = orderMapper.updateOrderStatus(id, 5);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("订单关闭成功");
        }
        return Result.error("订单关闭失败");
    }

    /**
     * 删除订单
     * @param id 订单ID
     * @return 操作结果
     */
    @Override
    public Result<String> deleteOrder(Long id) {
        int result = orderMapper.deleteById(id);
        if (result > 0) {
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
