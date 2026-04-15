package com.mall.entity;

import com.mall.common.BaseEntity;

/**
 * 商品分类实体类
 * 对应数据库表：category
 * 
 * @author mall
 * @date 2024-01-01
 */
public class Category extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;
    
    /** 分类名称 */
    private String name;
    
    /** 父分类ID */
    private Long parentId;
    
    /** 排序 */
    private Integer sort;
    
    /** 状态：0-禁用，1-启用 */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
