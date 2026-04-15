package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Logistics;
import com.mall.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/logistics")
public class LogisticsController {

    @Autowired
    private LogisticsService logisticsService;

    @GetMapping("/list")
    public Result<List<Logistics>> getLogisticsList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer shippingStatus) {
        return logisticsService.getLogisticsList(orderNo, shippingStatus);
    }

    @GetMapping("/{id}")
    public Result<Logistics> getLogisticsById(@PathVariable Long id) {
        return logisticsService.getLogisticsById(id);
    }

    @GetMapping("/order/{orderId}")
    public Result<Logistics> getLogisticsByOrderId(@PathVariable Long orderId) {
        return logisticsService.getLogisticsByOrderId(orderId);
    }

    @PostMapping
    public Result<String> addLogistics(@RequestBody Logistics logistics) {
        return logisticsService.addLogistics(logistics);
    }

    @PutMapping
    public Result<String> updateLogistics(@RequestBody Logistics logistics) {
        return logisticsService.updateLogistics(logistics);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteLogistics(@PathVariable Long id) {
        return logisticsService.deleteLogistics(id);
    }
}
