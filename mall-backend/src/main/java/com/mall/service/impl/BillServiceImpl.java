package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.Bill;
import com.mall.mapper.BillMapper;
import com.mall.service.BillService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 账单服务实现类
 * 
 * @author mall
 * @date 2024-01-01
 */
@Service
public class BillServiceImpl implements BillService {

    @Resource
    private BillMapper billMapper;

    /**
     * 根据ID查询账单
     */
    @Override
    public Result<Bill> getBillById(Long id) {
        Bill bill = billMapper.selectById(id);
        return Result.success(bill);
    }

    /**
     * 查询账单列表
     */
    @Override
    public Result<List<Bill>> getBillList(String billNo, String orderNo, Integer billType, Integer billStatus) {
        List<Bill> billList = billMapper.selectList(billNo, orderNo, billType, billStatus);
        return Result.success(billList);
    }

    /**
     * 新增账单
     */
    @Override
    public Result<String> addBill(Bill bill) {
        int result = billMapper.insert(bill);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新账单
     */
    @Override
    public Result<String> updateBill(Bill bill) {
        int result = billMapper.update(bill);
        if (result > 0) {
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 删除账单
     */
    @Override
    public Result<String> deleteBill(Long id) {
        int result = billMapper.deleteById(id);
        if (result > 0) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 导出账单到Excel
     */
    @Override
    public void exportBills(HttpServletResponse response, String billNo, String orderNo, 
                           Integer billType, Integer billStatus) {
        List<Bill> billList = billMapper.selectList(billNo, orderNo, billType, billStatus);
        
        // 创建Excel工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("账单列表");
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            String[] headers = {"账单编号", "订单编号", "用户名", "账单类型", "金额", "账单状态", "账单日期", "备注"};
            
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;
            for (Bill bill : billList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(bill.getBillNo());
                row.createCell(1).setCellValue(bill.getOrderNo());
                row.createCell(2).setCellValue(bill.getUsername());
                row.createCell(3).setCellValue(bill.getBillType() == 1 ? "收入" : "支出");
                row.createCell(4).setCellValue(bill.getAmount().doubleValue());
                row.createCell(5).setCellValue(bill.getBillStatus() == 1 ? "待处理" : "已处理");
                row.createCell(6).setCellValue(bill.getBillDate() != null ? sdf.format(bill.getBillDate()) : "");
                row.createCell(7).setCellValue(bill.getRemark());
            }
            
            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("账单列表", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            
            // 写入响应流
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("导出Excel失败", e);
        }
    }
}
