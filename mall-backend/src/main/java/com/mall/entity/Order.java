package com.mall.entity;

import com.mall.common.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 * 对应数据库表：order
 * 
 * @author mall
 * @date 2024-01-01
 */
public class Order extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;
    
    /** 订单编号 */
    private String orderNo;
    
    /** 用户ID */
    private Long userId;
    
    /** 用户名 */
    private String username;
    
    /** 订单总金额 */
    private BigDecimal totalAmount;
    
    /** 实付金额 */
    private BigDecimal payAmount;
    
    /** 支付方式：1-微信，2-支付宝 */
    private Integer payType;
    
    /** 支付时间 */
    private Date payTime;
    
    /** 订单状态：1-待付款，2-待发货，3-已发货，4-已完成，5-已关闭 */
    private Integer orderStatus;
    
    /** 收货人 */
    private String consignee;
    
    /** 联系电话 */
    private String phone;
    
    /** 收货地址 */
    private String address;
    
    /** 订单备注 */
    private String remark;
    
    /** 订单明细列表 */
    private List<OrderItem> orderItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
