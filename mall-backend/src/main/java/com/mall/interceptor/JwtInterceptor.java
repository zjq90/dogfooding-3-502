package com.mall.interceptor;

import com.mall.common.Result;
import com.mall.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * JWT认证拦截器
 * 验证请求中的JWT Token是否有效
 * 
 * @author mall
 * @date 2024-01-01
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 在请求处理之前进行拦截，验证JWT Token
     * 
     * @param request HTTP请求
     * @param response HTTP响应
     * @param handler 处理器
     * @return 是否继续处理请求
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头中获取Authorization
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            writeErrorResponse(response, "未提供有效的认证信息");
            return false;
        }

        // 提取Token
        String token = authHeader.substring(TOKEN_PREFIX.length());
        
        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            writeErrorResponse(response, "认证信息已过期或无效");
            return false;
        }

        // 将用户信息存入请求属性中，供后续使用
        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        request.setAttribute("userId", userId);
        request.setAttribute("username", username);

        return true;
    }

    /**
     * 写入错误响应
     * 
     * @param response HTTP响应
     * @param message 错误消息
     * @throws Exception 异常
     */
    private void writeErrorResponse(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        PrintWriter writer = response.getWriter();
        Result<Void> result = Result.error(401, message);
        writer.write(new ObjectMapper().writeValueAsString(result));
        writer.flush();
        writer.close();
    }
}
