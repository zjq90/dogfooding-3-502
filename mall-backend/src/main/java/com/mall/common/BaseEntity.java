package com.mall.common;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 * 包含通用的创建时间和更新时间字段
 * 
 * @author mall
 * @date 2024-01-01
 */
public class BaseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /** 创建时间 */
    private Date createTime;
    
    /** 更新时间 */
    private Date updateTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
