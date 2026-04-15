package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.Bill;
import com.mall.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/list")
    public Result<List<Bill>> getBillList(
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer billType,
            @RequestParam(required = false) Integer billStatus) {
        return billService.getBillList(billNo, orderNo, billType, billStatus);
    }

    @GetMapping("/{id}")
    public Result<Bill> getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    @PostMapping
    public Result<String> addBill(@RequestBody Bill bill) {
        return billService.addBill(bill);
    }

    @PutMapping
    public Result<String> updateBill(@RequestBody Bill bill) {
        return billService.updateBill(bill);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteBill(@PathVariable Long id) {
        return billService.deleteBill(id);
    }

    @GetMapping("/export")
    public void exportBill(
            HttpServletResponse response,
            @RequestParam(required = false) String billNo,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer billType,
            @RequestParam(required = false) Integer billStatus) {
        billService.exportBill(response, billNo, orderNo, billType, billStatus);
    }
}
