package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 订单控制器
 * 处理订单管理相关请求
 * 
 * @author mall
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 根据ID查询订单
     * 
     * @param id 订单ID
     * @return 订单信息
     */
    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    /**
     * 查询订单列表
     * 
     * @param orderNo 订单编号（模糊查询）
     * @param username 用户名（模糊查询）
     * @param orderStatus 订单状态
     * @return 订单列表
     */
    @GetMapping("/list")
    public Result<List<Order>> getOrderList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer orderStatus) {
        return orderService.getOrderList(orderNo, username, orderStatus);
    }

    /**
     * 更新订单
     * 
     * @param order 订单信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateOrder(@RequestBody Order order) {
        return orderService.updateOrder(order);
    }

    /**
     * 关闭订单
     * 
     * @param id 订单ID
     * @return 操作结果
     */
    @PutMapping("/{id}/close")
    public Result<String> closeOrder(@PathVariable Long id) {
        return orderService.closeOrder(id);
    }

    /**
     * 删除订单
     * 
     * @param id 订单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
