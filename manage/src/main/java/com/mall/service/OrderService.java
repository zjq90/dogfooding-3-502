package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Order;

import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 * 定义订单相关的业务操作
 */
public interface OrderService {

    /**
     * 根据ID获取订单详情
     * @param id 订单ID
     * @return 订单信息
     */
    Result<Order> getOrderById(Long id);

    /**
     * 根据订单号获取订单
     * @param orderNo 订单号
     * @return 订单信息
     */
    Result<Order> getOrderByOrderNo(String orderNo);

    /**
     * 获取订单列表（带分页）
     * @param orderNo 订单号
     * @param username 用户名
     * @param orderStatus 订单状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 订单列表和分页信息
     */
    Result<Map<String, Object>> getOrderList(String orderNo, String username, Integer orderStatus,
                                             Integer page, Integer pageSize);

    /**
     * 获取订单统计数据
     * @return 统计数据
     */
    Result<Map<String, Object>> getOrderStatistics();

    /**
     * 更新订单信息
     * @param order 订单信息
     * @return 操作结果
     */
    Result<String> updateOrder(Order order);

    /**
     * 关闭订单
     * @param id 订单ID
     * @return 操作结果
     */
    Result<String> closeOrder(Long id);

    /**
     * 删除订单
     * @param id 订单ID
     * @return 操作结果
     */
    Result<String> deleteOrder(Long id);
}
