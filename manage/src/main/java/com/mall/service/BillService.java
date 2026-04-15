package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.Bill;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 账单服务接口
 * 定义账单相关的业务操作
 */
public interface BillService {

    /**
     * 根据ID获取账单详情
     * @param id 账单ID
     * @return 账单信息
     */
    Result<Bill> getBillById(Long id);

    /**
     * 根据账单号获取账单
     * @param billNo 账单号
     * @return 账单信息
     */
    Result<Bill> getBillByBillNo(String billNo);

    /**
     * 获取账单列表（带分页）
     * @param billNo 账单号
     * @param orderNo 订单号
     * @param billType 账单类型
     * @param billStatus 账单状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 账单列表和分页信息
     */
    Result<Map<String, Object>> getBillList(String billNo, String orderNo, Integer billType, Integer billStatus,
                                             Integer page, Integer pageSize);

    /**
     * 新增账单
     * @param bill 账单信息
     * @return 操作结果
     */
    Result<String> addBill(Bill bill);

    /**
     * 更新账单信息
     * @param bill 账单信息
     * @return 操作结果
     */
    Result<String> updateBill(Bill bill);

    /**
     * 删除账单
     * @param id 账单ID
     * @return 操作结果
     */
    Result<String> deleteBill(Long id);

    /**
     * 导出账单Excel
     * @param response HTTP响应
     * @param billNo 账单号
     * @param orderNo 订单号
     * @param billType 账单类型
     * @param billStatus 账单状态
     */
    void exportBill(HttpServletResponse response, String billNo, String orderNo, Integer billType, Integer billStatus);
}
