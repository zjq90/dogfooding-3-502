package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Logistics;
import com.mall.service.LogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 物流管理控制器
 * 处理物流信息的增删改查操作
 */
@RestController
@RequestMapping("/api/logistics")
public class LogisticsController {

    @Autowired
    private LogisticsService logisticsService;

    /**
     * 获取物流列表
     * @param orderNo 订单号
     * @param shippingStatus 发货状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 物流列表（带分页）
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getLogisticsList(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer shippingStatus,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return logisticsService.getLogisticsList(orderNo, shippingStatus, page, pageSize);
    }

    /**
     * 根据ID获取物流详情
     * @param id 物流ID
     * @return 物流信息
     */
    @GetMapping("/{id}")
    public Result<Logistics> getLogisticsById(@PathVariable Long id) {
        return logisticsService.getLogisticsById(id);
    }

    /**
     * 根据订单ID获取物流信息
     * @param orderId 订单ID
     * @return 物流信息
     */
    @GetMapping("/order/{orderId}")
    public Result<Logistics> getLogisticsByOrderId(@PathVariable Long orderId) {
        return logisticsService.getLogisticsByOrderId(orderId);
    }

    /**
     * 新增物流信息
     * @param logistics 物流信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addLogistics(@RequestBody Logistics logistics) {
        return logisticsService.addLogistics(logistics);
    }

    /**
     * 更新物流信息
     * @param logistics 物流信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateLogistics(@RequestBody Logistics logistics) {
        return logisticsService.updateLogistics(logistics);
    }

    /**
     * 删除物流信息
     * @param id 物流ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteLogistics(@PathVariable Long id) {
        return logisticsService.deleteLogistics(id);
    }
}
