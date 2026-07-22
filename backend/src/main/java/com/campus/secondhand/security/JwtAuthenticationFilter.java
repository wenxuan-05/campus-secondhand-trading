package com.campus.secondhand.security;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.campus.secondhand.common.Result;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final UserMapper userMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            Long userId = jwtTokenProvider.getUserId(token);
            String studentId = jwtTokenProvider.getStudentId(token);
            String role = jwtTokenProvider.getRole(token);
            if (!StringUtils.hasText(role)) {
                role = "USER"; // 兼容旧token（无role字段）
            }

            // 校验Token版本号
            int tokenVersion = jwtTokenProvider.getTokenVersion(token);
            User user = userMapper.selectById(userId);
            if (user == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(401);
                objectMapper.writeValue(response.getWriter(), Result.unauthorized("用户不存在"));
                return;
            }
            int currentVersion = user.getTokenVersion() != null ? user.getTokenVersion() : 0;
            if (tokenVersion != currentVersion) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(401);
                objectMapper.writeValue(response.getWriter(), Result.unauthorized("密码已修改，请重新登录"));
                return;
            }

            // 同步数据库最新角色（管理员可能在token有效期内修改了角色）
            if (user.getRole() != null && !user.getRole().equals(role)) {
                role = user.getRole();
            }

            UserContext.setCurrentUser(userId, studentId, role);
            try {
                filterChain.doFilter(request, response);
            } finally {
                UserContext.clear();
            }
        } else if (requiresAuth(request)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            objectMapper.writeValue(response.getWriter(), Result.unauthorized("未登录或token已过期"));
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return request.getParameter("token");
    }

    private boolean requiresAuth(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        // Public endpoints: login, register, send-code, reset-password, WebSocket, file access, and browsing goods
        if (path.contains("/api/user/login") || path.contains("/api/user/register")) return false;
        if (path.contains("/api/user/send-code") || path.contains("/api/user/reset-password")) return false;
        if (path.startsWith("/ws/")) return false;
        if (path.contains("/api/file/")) return false;
        if ("OPTIONS".equals(method)) return false;
        // Allow unauthenticated browsing of goods (search & detail)
        if ("GET".equals(method) && path.matches("/api/goods/\\d+")) return false;  // GET /api/goods/{id}
        if ("GET".equals(method) && path.equals("/api/goods/search")) return false; // GET /api/goods/search
        if ("GET".equals(method) && path.startsWith("/api/recommend")) return false;    // GET /api/recommend*
        if ("GET".equals(method) && path.matches("/api/user/\\d+")) return false;   // GET /api/user/{id} (seller info)
        return true;
    }
}
