package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Logistics;

import java.util.Map;

/**
 * 物流服务接口
 * 定义物流相关的业务操作
 */
public interface LogisticsService {

    /**
     * 根据ID获取物流详情
     * @param id 物流ID
     * @return 物流信息
     */
    Result<Logistics> getLogisticsById(Long id);

    /**
     * 根据订单ID获取物流信息
     * @param orderId 订单ID
     * @return 物流信息
     */
    Result<Logistics> getLogisticsByOrderId(Long orderId);

    /**
     * 获取物流列表（带分页）
     * @param orderNo 订单号
     * @param shippingStatus 发货状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 物流列表和分页信息
     */
    Result<Map<String, Object>> getLogisticsList(String orderNo, Integer shippingStatus,
                                                   Integer page, Integer pageSize);

    /**
     * 新增物流信息
     * @param logistics 物流信息
     * @return 操作结果
     */
    Result<String> addLogistics(Logistics logistics);

    /**
     * 更新物流信息
     * @param logistics 物流信息
     * @return 操作结果
     */
    Result<String> updateLogistics(Logistics logistics);

    /**
     * 删除物流信息
     * @param id 物流ID
     * @return 操作结果
     */
    Result<String> deleteLogistics(Long id);
}
