package com.mall.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mall.common.Result;
import com.mall.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT登录拦截器
 * 验证用户请求中的token是否有效
 */
public class LoginInterceptor implements HandlerInterceptor {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 预检请求放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        String uri = request.getRequestURI();
        
        // 登录接口放行
        if (uri.contains("/api/auth/login")) {
            return true;
        }

        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        
        if (token == null || !token.startsWith("Bearer ")) {
            writeErrorResponse(response, 401, "未登录或token无效");
            return false;
        }

        // 去掉"Bearer "前缀
        token = token.substring(7);

        try {
            // 验证token
            if (!JwtUtils.validateToken(token)) {
                writeErrorResponse(response, 401, "token已过期或无效");
                return false;
            }

            // 解析token中的用户信息，存入request属性中
            Claims claims = JwtUtils.parseToken(token);
            request.setAttribute("userId", claims.get("userId", Long.class));
            request.setAttribute("username", claims.get("username", String.class));

            return true;
        } catch (Exception e) {
            writeErrorResponse(response, 401, "token验证失败");
            return false;
        }
    }

    /**
     * 写入错误响应
     */
    private void writeErrorResponse(HttpServletResponse response, Integer code, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Result<String> result = Result.error(code, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
