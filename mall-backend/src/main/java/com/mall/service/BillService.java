package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Bill;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 账单服务接口
 * 
 * @author mall
 * @date 2024-01-01
 */
public interface BillService {

    /**
     * 根据ID查询账单
     * 
     * @param id 账单ID
     * @return 账单信息
     */
    Result<Bill> getBillById(Long id);

    /**
     * 查询账单列表
     * 
     * @param billNo 账单编号（模糊查询）
     * @param orderNo 订单编号（模糊查询）
     * @param billType 账单类型
     * @param billStatus 账单状态
     * @return 账单列表
     */
    Result<List<Bill>> getBillList(String billNo, String orderNo, Integer billType, Integer billStatus);

    /**
     * 新增账单
     * 
     * @param bill 账单信息
     * @return 操作结果
     */
    Result<String> addBill(Bill bill);

    /**
     * 更新账单
     * 
     * @param bill 账单信息
     * @return 操作结果
     */
    Result<String> updateBill(Bill bill);

    /**
     * 删除账单
     * 
     * @param id 账单ID
     * @return 操作结果
     */
    Result<String> deleteBill(Long id);

    /**
     * 导出账单到Excel
     * 
     * @param response HTTP响应
     * @param billNo 账单编号
     * @param orderNo 订单编号
     * @param billType 账单类型
     * @param billStatus 账单状态
     */
    void exportBills(HttpServletResponse response, String billNo, String orderNo, 
                     Integer billType, Integer billStatus);
}
