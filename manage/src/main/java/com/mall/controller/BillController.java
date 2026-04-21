package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Bill;
import com.mall.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 账单管理控制器
 * 处理账单的增删改查和导出功能
 */
@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    private BillService billService;

    /**
     * 获取账单列表
     * @param billNo 账单号
     * @param orderNo 订单号
     * @param billType 账单类型
     * @param billStatus 账单状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 账单列表（带分页）
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getBillList(
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer billType,
            @RequestParam(required = false) Integer billStatus,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return billService.getBillList(billNo, orderNo, billType, billStatus, page, pageSize);
    }

    /**
     * 根据ID获取账单详情
     * @param id 账单ID
     * @return 账单信息
     */
    @GetMapping("/{id}")
    public Result<Bill> getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    /**
     * 新增账单
     * @param bill 账单信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addBill(@RequestBody Bill bill) {
        return billService.addBill(bill);
    }

    /**
     * 更新账单信息
     * @param bill 账单信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateBill(@RequestBody Bill bill) {
        return billService.updateBill(bill);
    }

    /**
     * 删除账单
     * @param id 账单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteBill(@PathVariable Long id) {
        return billService.deleteBill(id);
    }

    /**
     * 导出账单Excel
     * @param response HTTP响应
     * @param billNo 账单号
     * @param orderNo 订单号
     * @param billType 账单类型
     * @param billStatus 账单状态
     */
    @GetMapping("/export")
    public void exportBill(HttpServletResponse response,
                           @RequestParam(required = false) String billNo,
                           @RequestParam(required = false) String orderNo,
                           @RequestParam(required = false) Integer billType,
                           @RequestParam(required = false) Integer billStatus) {
        billService.exportBill(response, billNo, orderNo, billType, billStatus);
    }
}
