package com.mall.mapper;

import com.mall.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单明细数据访问层
 * 
 * @author mall
 * @date 2024-01-01
 */
@Mapper
public interface OrderItemMapper {

    /**
     * 根据订单ID查询订单明细
     * 
     * @param orderId 订单ID
     * @return 订单明细列表
     */
    List<OrderItem> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 批量新增订单明细
     * 
     * @param orderItems 订单明细列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<OrderItem> orderItems);

    /**
     * 删除订单明细
     * 
     * @param orderId 订单ID
     * @return 影响行数
     */
    int deleteByOrderId(@Param("orderId") Long orderId);
}
