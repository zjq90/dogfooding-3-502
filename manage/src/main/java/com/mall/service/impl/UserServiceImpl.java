package com.mall.service.impl;

import com.mall.common.Result;
import com.mall.entity.User;
import com.mall.mapper.UserMapper;
import com.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 * 实现用户相关的业务逻辑
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Redis缓存前缀
    private static final String REDIS_KEY_PREFIX = "user:";
    // Redis过期时间（分钟）
    private static final long REDIS_EXPIRE_TIME = 30;

    /**
     * 用户登录验证
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    @Override
    public Result<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return Result.error("用户不存在");
        }
        // MD5加密密码验证
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Password.equals(user.getPassword())) {
            return Result.error("密码错误");
        }
        if (user.getStatus() == 0) {
            return Result.error("用户已被禁用");
        }
        // 清除敏感信息
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 根据ID获取用户信息（带Redis缓存）
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public Result<User> getUserById(Long id) {
        String redisKey = REDIS_KEY_PREFIX + id;
        User user = (User) redisTemplate.opsForValue().get(redisKey);
        if (user == null) {
            user = userMapper.selectById(id);
            if (user != null) {
                user.setPassword(null);
                redisTemplate.opsForValue().set(redisKey, user, REDIS_EXPIRE_TIME, TimeUnit.MINUTES);
            }
        }
        return Result.success(user);
    }

    /**
     * 获取用户列表（带分页）
     * @param username 用户名（模糊查询）
     * @param status 用户状态
     * @param page 页码
     * @param pageSize 每页条数
     * @return 用户列表和分页信息
     */
    @Override
    public Result<Map<String, Object>> getUserList(String username, Integer status, Integer page, Integer pageSize) {
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        List<User> userList = userMapper.selectListWithPage(username, status, offset, pageSize);
        // 清除密码信息
        userList.forEach(u -> u.setPassword(null));
        int total = userMapper.selectCount(username, status);

        Map<String, Object> result = new HashMap<>();
        result.put("list", userList);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);

        return Result.success(result);
    }

    /**
     * 新增用户
     * @param user 用户信息
     * @return 操作结果
     */
    @Override
    public Result<String> addUser(User user) {
        User existUser = userMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            return Result.error("用户名已存在");
        }
        // MD5加密密码
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);
        int result = userMapper.insert(user);
        if (result > 0) {
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 操作结果
     */
    @Override
    public Result<String> updateUser(User user) {
        // 如果更新密码，进行MD5加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
            user.setPassword(md5Password);
        }
        int result = userMapper.update(user);
        if (result > 0) {
            // 清除缓存
            String redisKey = REDIS_KEY_PREFIX + user.getId();
            redisTemplate.delete(redisKey);
            return Result.success("更新成功");
        }
        return Result.error("更新失败");
    }

    /**
     * 更新用户状态
     * @param id 用户ID
     * @param status 用户状态
     * @return 操作结果
     */
    @Override
    public Result<String> updateUserStatus(Long id, Integer status) {
        int result = userMapper.updateStatus(id, status);
        if (result > 0) {
            // 清除缓存
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("状态更新成功");
        }
        return Result.error("状态更新失败");
    }

    /**
     * 删除用户
     * @param id 用户ID
     * @return 操作结果
     */
    @Override
    public Result<String> deleteUser(Long id) {
        int result = userMapper.deleteById(id);
        if (result > 0) {
            // 清除缓存
            String redisKey = REDIS_KEY_PREFIX + id;
            redisTemplate.delete(redisKey);
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }
}
