package com.mall.controller;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result<List<User>> getUserList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status) {
        return userService.getUserList(username, status);
    }

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public Result<String> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public Result<String> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/status")
    public Result<String> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        return userService.updateUserStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
