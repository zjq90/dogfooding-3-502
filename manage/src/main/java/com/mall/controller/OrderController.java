package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Order;
import com.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public Result<List<Order>> getOrderList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer orderStatus) {
        return orderService.getOrderList(orderNo, username, orderStatus);
    }

    @GetMapping("/{id}")
    public Result<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping
    public Result<String> updateOrder(@RequestBody Order order) {
        return orderService.updateOrder(order);
    }

    @PutMapping("/{id}/close")
    public Result<String> closeOrder(@PathVariable Long id) {
        return orderService.closeOrder(id);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteOrder(@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }
}
