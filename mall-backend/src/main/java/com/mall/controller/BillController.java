package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Bill;
import com.mall.service.BillService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 账单控制器
 * 处理账单管理相关请求
 * 
 * @author mall
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Resource
    private BillService billService;

    /**
     * 根据ID查询账单
     * 
     * @param id 账单ID
     * @return 账单信息
     */
    @GetMapping("/{id}")
    public Result<Bill> getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    /**
     * 查询账单列表
     * 
     * @param billNo 账单编号（模糊查询）
     * @param orderNo 订单编号（模糊查询）
     * @param billType 账单类型
     * @param billStatus 账单状态
     * @return 账单列表
     */
    @GetMapping("/list")
    public Result<List<Bill>> getBillList(
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer billType,
            @RequestParam(required = false) Integer billStatus) {
        return billService.getBillList(billNo, orderNo, billType, billStatus);
    }

    /**
     * 新增账单
     * 
     * @param bill 账单信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addBill(@RequestBody Bill bill) {
        return billService.addBill(bill);
    }

    /**
     * 更新账单
     * 
     * @param bill 账单信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateBill(@RequestBody Bill bill) {
        return billService.updateBill(bill);
    }

    /**
     * 删除账单
     * 
     * @param id 账单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteBill(@PathVariable Long id) {
        return billService.deleteBill(id);
    }

    /**
     * 导出账单到Excel
     * 
     * @param response HTTP响应
     * @param billNo 账单编号
     * @param orderNo 订单编号
     * @param billType 账单类型
     * @param billStatus 账单状态
     */
    @GetMapping("/export")
    public void exportBills(
            HttpServletResponse response,
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer billType,
            @RequestParam(required = false) Integer billStatus) {
        billService.exportBills(response, billNo, orderNo, billType, billStatus);
    }
}
