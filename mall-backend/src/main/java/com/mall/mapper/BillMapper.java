package com.mall.mapper;

import com.mall.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 账单数据访问层
 * 
 * @author mall
 * @date 2024-01-01
 */
@Mapper
public interface BillMapper {

    /**
     * 根据ID查询账单
     * 
     * @param id 账单ID
     * @return 账单信息
     */
    Bill selectById(@Param("id") Long id);

    /**
     * 查询账单列表
     * 
     * @param billNo 账单编号（模糊查询）
     * @param orderNo 订单编号（模糊查询）
     * @param billType 账单类型
     * @param billStatus 账单状态
     * @return 账单列表
     */
    List<Bill> selectList(@Param("billNo") String billNo, 
                         @Param("orderNo") String orderNo, 
                         @Param("billType") Integer billType, 
                         @Param("billStatus") Integer billStatus);

    /**
     * 新增账单
     * 
     * @param bill 账单信息
     * @return 影响行数
     */
    int insert(Bill bill);

    /**
     * 更新账单
     * 
     * @param bill 账单信息
     * @return 影响行数
     */
    int update(Bill bill);

    /**
     * 删除账单
     * 
     * @param id 账单ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
