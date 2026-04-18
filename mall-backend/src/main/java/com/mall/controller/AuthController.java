package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理登录、注册等认证相关请求
 * 
 * @author mall
 * @date 2024-01-01
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * 
     * @param loginRequest 登录请求参数
     * @return 登录结果，包含Token
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Result<String> tokenResult = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        
        if (tokenResult.getCode() != 200) {
            return Result.error(tokenResult.getMessage());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", tokenResult.getData());
        result.put("tokenType", "Bearer");
        
        return Result.success(result);
    }

    /**
     * 用户注册
     * 
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        // 默认角色为普通用户
        user.setRole(2);
        // 默认状态为启用
        user.setStatus(1);
        return userService.addUser(user);
    }

    /**
     * 登录请求参数
     */
    public static class LoginRequest {
        private String username;
        private String password;

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
    }
}
