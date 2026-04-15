package com.mall.entity;

import com.mall.common.BaseEntity;

/**
 * 用户实体类
 * 对应数据库表：user
 * 
 * @author mall
 * @date 2024-01-01
 */
public class User extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;
    
    /** 用户名 */
    private String username;
    
    /** 密码 */
    private String password;
    
    /** 真实姓名 */
    private String realName;
    
    /** 手机号 */
    private String phone;
    
    /** 邮箱 */
    private String email;
    
    /** 头像 */
    private String avatar;
    
    /** 角色：1-管理员，2-普通用户 */
    private Integer role;
    
    /** 状态：0-禁用，1-启用 */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
