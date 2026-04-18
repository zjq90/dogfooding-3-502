package com.mall.mapper;

import com.mall.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问层
 * 
 * @author mall
 * @date 2024-01-01
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户信息
     */
    User selectByUsername(@Param("username") String username);

    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return 用户信息
     */
    User selectById(@Param("id") Long id);

    /**
     * 查询用户列表
     * 
     * @param username 用户名（模糊查询）
     * @param status 状态
     * @return 用户列表
     */
    List<User> selectList(@Param("username") String username, @Param("status") Integer status);

    /**
     * 新增用户
     * 
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户
     * 
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);

    /**
     * 更新用户状态
     * 
     * @param id 用户ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("id") Long id);
}
