package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 * 处理用户的增删改查操作
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     * @param username 用户名（模糊查询）
     * @param status 用户状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 用户列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getUserList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return userService.getUserList(username, status, page, pageSize);
    }

    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * 新增用户
     * @param user 用户信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 操作结果
     */
    @PutMapping
    public Result<String> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * 更新用户状态
     * @param id 用户ID
     * @param status 用户状态
     * @return 操作结果
     */
    @PutMapping("/{id}/status")
    public Result<String> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        return userService.updateUserStatus(id, status);
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
