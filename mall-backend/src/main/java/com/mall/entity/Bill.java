package com.mall.entity;

import com.mall.common.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账单实体类
 * 对应数据库表：bill
 * 
 * @author mall
 * @date 2024-01-01
 */
public class Bill extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;
    
    /** 账单编号 */
    private String billNo;
    
    /** 订单编号 */
    private String orderNo;
    
    /** 用户ID */
    private Long userId;
    
    /** 用户名 */
    private String username;
    
    /** 账单类型：1-收入，2-支出 */
    private Integer billType;
    
    /** 账单金额 */
    private BigDecimal amount;
    
    /** 账单状态：1-待处理，2-已处理 */
    private Integer billStatus;
    
    /** 账单日期 */
    private Date billDate;
    
    /** 备注 */
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Integer billStatus) {
        this.billStatus = billStatus;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
