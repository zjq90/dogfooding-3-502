package com.mall.mapper;

import com.mall.entity.Logistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物流数据访问层
 * 
 * @author mall
 * @date 2024-01-01
 */
@Mapper
public interface LogisticsMapper {

    /**
     * 根据ID查询物流信息
     * 
     * @param id 物流ID
     * @return 物流信息
     */
    Logistics selectById(@Param("id") Long id);

    /**
     * 根据订单ID查询物流信息
     * 
     * @param orderId 订单ID
     * @return 物流信息
     */
    Logistics selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询物流列表
     * 
     * @param orderNo 订单编号（模糊查询）
     * @param shippingStatus 物流状态
     * @return 物流列表
     */
    List<Logistics> selectList(@Param("orderNo") String orderNo, 
                              @Param("shippingStatus") Integer shippingStatus);

    /**
     * 新增物流信息
     * 
     * @param logistics 物流信息
     * @return 影响行数
     */
    int insert(Logistics logistics);

    /**
     * 更新物流信息
     * 
     * @param logistics 物流信息
     * @return 影响行数
     */
    int update(Logistics logistics);

    /**
     * 删除物流信息
     * 
     * @param id 物流ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
