package com.mall.mapper;

import com.mall.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单数据访问层
 * 
 * @author mall
 * @date 2024-01-01
 */
@Mapper
public interface OrderMapper {

    /**
     * 根据ID查询订单
     * 
     * @param id 订单ID
     * @return 订单信息
     */
    Order selectById(@Param("id") Long id);

    /**
     * 查询订单列表
     * 
     * @param orderNo 订单编号（模糊查询）
     * @param username 用户名（模糊查询）
     * @param orderStatus 订单状态
     * @return 订单列表
     */
    List<Order> selectList(@Param("orderNo") String orderNo, 
                          @Param("username") String username, 
                          @Param("orderStatus") Integer orderStatus);

    /**
     * 新增订单
     * 
     * @param order 订单信息
     * @return 影响行数
     */
    int insert(Order order);

    /**
     * 更新订单
     * 
     * @param order 订单信息
     * @return 影响行数
     */
    int update(Order order);

    /**
     * 更新订单状态
     * 
     * @param id 订单ID
     * @param orderStatus 订单状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("orderStatus") Integer orderStatus);

    /**
     * 删除订单
     * 
     * @param id 订单ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
