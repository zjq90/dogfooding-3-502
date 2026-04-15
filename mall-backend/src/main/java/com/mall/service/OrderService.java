package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Order;

import java.util.List;

/**
 * 订单服务接口
 * 
 * @author mall
 * @date 2024-01-01
 */
public interface OrderService {

    /**
     * 根据ID查询订单
     * 
     * @param id 订单ID
     * @return 订单信息
     */
    Result<Order> getOrderById(Long id);

    /**
     * 查询订单列表
     * 
     * @param orderNo 订单编号（模糊查询）
     * @param username 用户名（模糊查询）
     * @param orderStatus 订单状态
     * @return 订单列表
     */
    Result<List<Order>> getOrderList(String orderNo, String username, Integer orderStatus);

    /**
     * 更新订单
     * 
     * @param order 订单信息
     * @return 操作结果
     */
    Result<String> updateOrder(Order order);

    /**
     * 关闭订单
     * 
     * @param id 订单ID
     * @return 操作结果
     */
    Result<String> closeOrder(Long id);

    /**
     * 删除订单
     * 
     * @param id 订单ID
     * @return 操作结果
     */
    Result<String> deleteOrder(Long id);
}
