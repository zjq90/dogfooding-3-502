package com.mall.entity;

import com.mall.common.BaseEntity;

/**
 * 物流实体类
 * 对应数据库表：logistics
 * 
 * @author mall
 * @date 2024-01-01
 */
public class Logistics extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;
    
    /** 订单ID */
    private Long orderId;
    
    /** 订单编号 */
    private String orderNo;
    
    /** 物流公司 */
    private String shippingCompany;
    
    /** 物流单号 */
    private String shippingNo;
    
    /** 物流状态：1-待发货，2-已发货，3-运输中，4-已签收 */
    private Integer shippingStatus;
    
    /** 物流跟踪信息 */
    private String trackingInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(String shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public String getShippingNo() {
        return shippingNo;
    }

    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    public Integer getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(Integer shippingStatus) {
        this.shippingStatus = shippingStatus;
    }

    public String getTrackingInfo() {
        return trackingInfo;
    }

    public void setTrackingInfo(String trackingInfo) {
        this.trackingInfo = trackingInfo;
    }
}
