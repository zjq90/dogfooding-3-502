package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import com.mall.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录认证控制器
 * 处理用户登录、登出等认证相关操作
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录接口
     * @param user 包含用户名和密码的用户对象
     * @return 登录结果，包含token和用户信息
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        Result<User> result = userService.login(user.getUsername(), user.getPassword());
        
        if (result.getCode() != 200) {
            return Result.error(result.getMessage());
        }
        
        User loginUser = result.getData();
        
        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUser.getId());
        claims.put("username", loginUser.getUsername());
        String token = JwtUtils.generateToken(claims);
        
        // 返回token和用户信息
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", loginUser);
        
        return Result.success(data);
    }

    /**
     * 用户登出接口
     * @return 登出结果
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("登出成功");
    }

    /**
     * 获取当前登录用户信息
     * @param userId 从token中解析的用户ID
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<User> getUserInfo(@RequestAttribute("userId") Long userId) {
        return userService.getUserById(userId);
    }
}
