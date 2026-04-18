package com.mall.service;

import com.mall.common.Result;
import com.mall.entity.User;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author mall
 * @date 2024-01-01
 */
public interface UserService {

    /**
     * 用户登录
     * 
     * @param username 用户名
     * @param password 密码
     * @return 登录结果，包含Token
     */
    Result<String> login(String username, String password);

    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    Result<User> getUserById(Long id);

    /**
     * 查询用户列表
     * 
     * @param username 用户名（模糊查询）
     * @param status 状态
     * @return 用户列表
     */
    Result<List<User>> getUserList(String username, Integer status);

    /**
     * 新增用户
     * 
     * @param user 用户信息
     * @return 操作结果
     */
    Result<String> addUser(User user);

    /**
     * 更新用户
     * 
     * @param user 用户信息
     * @return 操作结果
     */
    Result<String> updateUser(User user);

    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 状态
     * @return 操作结果
     */
    Result<String> updateUserStatus(Long id, Integer status);

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 操作结果
     */
    Result<String> deleteUser(Long id);
}
