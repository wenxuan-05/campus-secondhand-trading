package com.campustrade.controller;

import com.campustrade.dto.*;
import com.campustrade.service.AuthService;
import com.campustrade.util.JwtUtil;
import com.campustrade.vo.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/register")
    public Result<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/auth/login")
    public Result<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/user/verify")
    public Result<UserProfileDTO> verifyStudent(
            @Valid @RequestBody StudentAuthRequest request,
            HttpServletRequest httpRequest) {
        Long userId = getCurrentUserId(httpRequest);
        return authService.verifyStudent(userId, request);
    }

    @GetMapping("/user/profile")
    public Result<UserProfileDTO> getProfile(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        return authService.getProfile(userId);
    }

    @PostMapping("/auth/refresh")
    public Result<AuthResponse> refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    @PostMapping("/auth/logout")
    public Result<Void> logout(HttpServletRequest request) {
        return Result.success();
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserId(token);
        }
        throw new RuntimeException("未登录");
    }
}