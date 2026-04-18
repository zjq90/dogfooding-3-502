package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户控制器
 * 处理用户管理相关请求
 * 
 * @author mall
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 获取当前登录用户信息
     * 
     * @param request HTTP请求
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return userService.getUserById(userId);
    }

    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 查询用户列表
     * 
     * @param username 用户名（模糊查询）
     * @param status 状态
     * @return 用户列表
     */
    @GetMapping("/list")
    public Result<List<User>> getUserList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        return userService.getUserList(username, status);
    }

    /**
     * 新增用户
     * 
     * @param user 用户信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * 更新用户
     * 
     * @param user 用户信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 状态
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<String> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return userService.updateUserStatus(id, status);
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
