package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 订单管理控制器
 * 处理订单的查询、更新、关闭等操作
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 获取订单列表
     * @param orderNo 订单号
     * @param username 用户名
     * @param orderStatus 订单状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 订单列表（带分页）
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getOrderList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return orderService.getOrderList(orderNo, username, orderStatus, page, pageSize);
    }

    /**
     * 根据ID获取订单详情
     * @param id 订单ID
     * @return 订单信息
     */
    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    /**
     * 获取订单统计数据
     * @return 统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getOrderStatistics() {
        return orderService.getOrderStatistics();
    }

    /**
     * 更新订单信息
     * @param order 订单信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateOrder(@RequestBody Order order) {
        return orderService.updateOrder(order);
    }

    /**
     * 关闭订单
     * @param id 订单ID
     * @return 操作结果
     */
    @PutMapping("/{id}/close")
    public Result<String> closeOrder(@PathVariable Long id) {
        return orderService.closeOrder(id);
    }

    /**
     * 删除订单
     * @param id 订单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
