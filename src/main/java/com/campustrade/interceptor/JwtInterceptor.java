package com.campustrade.interceptor;

import com.campustrade.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        if (uri.contains("/auth/login") || uri.contains("/auth/register") || uri.contains("/auth/refresh")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(401);
            return false;
        }

        token = token.substring(7);
        if (!jwtUtil.validateToken(token) || !jwtUtil.isAccessToken(token)) {
            response.setStatus(401);
            return false;
        }

        request.setAttribute("userId", jwtUtil.getUserId(token));
        return true;
    }
}
