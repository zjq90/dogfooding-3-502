package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Logistics;
import com.mall.service.LogisticsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 物流控制器
 * 处理物流管理相关请求
 * 
 * @author mall
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {

    @Resource
    private LogisticsService logisticsService;

    /**
     * 根据ID查询物流信息
     * 
     * @param id 物流ID
     * @return 物流信息
     */
    @GetMapping("/{id}")
    public Result<Logistics> getLogisticsById(@PathVariable Long id) {
        return logisticsService.getLogisticsById(id);
    }

    /**
     * 根据订单ID查询物流信息
     * 
     * @param orderId 订单ID
     * @return 物流信息
     */
    @GetMapping("/order/{orderId}")
    public Result<Logistics> getLogisticsByOrderId(@PathVariable Long orderId) {
        return logisticsService.getLogisticsByOrderId(orderId);
    }

    /**
     * 查询物流列表
     * 
     * @param orderNo 订单编号（模糊查询）
     * @param shippingStatus 物流状态
     * @return 物流列表
     */
    @GetMapping("/list")
    public Result<List<Logistics>> getLogisticsList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer shippingStatus) {
        return logisticsService.getLogisticsList(orderNo, shippingStatus);
    }

    /**
     * 新增物流信息
     * 
     * @param logistics 物流信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addLogistics(@RequestBody Logistics logistics) {
        return logisticsService.addLogistics(logistics);
    }

    /**
     * 更新物流信息
     * 
     * @param logistics 物流信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateLogistics(@RequestBody Logistics logistics) {
        return logisticsService.updateLogistics(logistics);
    }

    /**
     * 删除物流信息
     * 
     * @param id 物流ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteLogistics(@PathVariable Long id) {
        return logisticsService.deleteLogistics(id);
    }
}
