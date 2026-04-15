package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.mapper.UserMapper;
import com.mall.service.UserService;
import com.mall.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 * 
 * @author mall
 * @date 2024-01-01
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtUtil jwtUtil;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /** Redis缓存前缀 */
    private static final String REDIS_KEY_PREFIX = "user:";
    
    /** Redis缓存过期时间（分钟） */
    private static final long REDIS_EXPIRE_TIME = 30;

    /**
     * 用户登录
     */
    @Override
    public Result<String> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(user.getPassword())) {
            return Result.error("密码错误");
        }
        
        if (user.getStatus() == 0) {
            return Result.error("用户已被禁用");
        }
        
        // 生成JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 将用户信息存入Redis
        user.setPassword(null);
        String redisKey = REDIS_KEY_PREFIX + user.getId();
        redisTemplate.opsForValue().set(redisKey, user, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
        
        return Result.success(token);
    }

    /**
     * 根据ID查询用户
     */
    @Override
    public Result<User> getUserById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        
        // 先从Redis查询
        User user = (User) redisTemplate.opsForValue().get(redisKey);
        if (user == null) {
            // Redis未命中，从数据库查询
            user = userMapper.selectById(id);
            if (user != null) {
                user.setPassword(null);
                redisTemplate.opsForValue().set(redisKey, user, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        
        return Result.success(user);
    }

    /**
     * 查询用户列表
     */
    @Override
    public Result<List<User>> getUserList(String username, Integer status) {
        List<User> userList = userMapper.selectList(username, status);
        // 清除密码信息
        userList.forEach(user -> user.setPassword(null));
        return Result.success(userList);
    }

    /**
     * 新增用户
     */
    @Override
    public Result<String> addUser(User user) {
        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }
        
        // 密码MD5加密
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        
        int result = userMapper.insert(user);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新用户
     */
    @Override
    public Result<String> updateUser(User user) {
        int result = userMapper.update(user);
        if (result > 0) {
            // 清除Redis缓存
            String redisKey = REDIS_KEY_PREFIX + user.getId();
            redisTemplate.delete(redisKey);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 更新用户状态
     */
    @Override
    public Result<String> updateUserStatus(Long id, Integer status) {
        int result = userMapper.updateStatus(id, status);
        if (result > 0) {
            // 清除Redis缓存
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }

    /**
     * 删除用户
     */
    @Override
    public Result<String> deleteUser(Long id) {
        int result = userMapper.deleteById(id);
        if (result > 0) {
            // 清除Redis缓存
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
