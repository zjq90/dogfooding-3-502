package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Logistics;

import java.util.List;

/**
 * 物流服务接口
 * 
 * @author mall
 * @date 2024-01-01
 */
public interface LogisticsService {

    /**
     * 根据ID查询物流信息
     * 
     * @param id 物流ID
     * @return 物流信息
     */
    Result<Logistics> getLogisticsById(Long id);

    /**
     * 根据订单ID查询物流信息
     * 
     * @param orderId 订单ID
     * @return 物流信息
     */
    Result<Logistics> getLogisticsByOrderId(Long orderId);

    /**
     * 查询物流列表
     * 
     * @param orderNo 订单编号（模糊查询）
     * @param shippingStatus 物流状态
     * @return 物流列表
     */
    Result<List<Logistics>> getLogisticsList(String orderNo, Integer shippingStatus);

    /**
     * 新增物流信息
     * 
     * @param logistics 物流信息
     * @return 操作结果
     */
    Result<String> addLogistics(Logistics logistics);

    /**
     * 更新物流信息
     * 
     * @param logistics 物流信息
     * @return 操作结果
     */
    Result<String> updateLogistics(Logistics logistics);

    /**
     * 删除物流信息
     * 
     * @param id 物流ID
     * @return 操作结果
     */
    Result<String> deleteLogistics(Long id);
}
