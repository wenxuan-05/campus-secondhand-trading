package com.campus.secondhand.security;

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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            Long userId = jwtTokenProvider.getUserId(token);
            String studentId = jwtTokenProvider.getStudentId(token);
            String role = jwtTokenProvider.getRole(token);
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
        // Public endpoints
        return !path.contains("/api/user/login")
                && !path.contains("/api/user/register")
                && !path.startsWith("/ws/")
                && !path.contains("/api/file/")
                && !"OPTIONS".equals(request.getMethod());
    }
}
