package com.mall.common;

import java.io.Serializable;
import java.util.List;

/**
 * 分页响应结果封装类
 * 
 * @param <T> 数据类型
 * @author mall
 * @date 2024-01-01
 */
public class PageResult<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
    private Long total;
    
    /** 当前页数据 */
    private List<T> list;
    
    /** 当前页码 */
    private Integer pageNum;
    
    /** 每页大小 */
    private Integer pageSize;
    
    /** 总页数 */
    private Integer totalPages;

    public PageResult() {
    }

    public PageResult(Long total, List<T> list, Integer pageNum, Integer pageSize) {
        this.total = total;
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }

    public static <T> PageResult<T> of(Long total, List<T> list, Integer pageNum, Integer pageSize) {
        return new PageResult<>(total, list, pageNum, pageSize);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
