package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.LoginDTO;
import com.campus.secondhand.dto.RegisterDTO;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.security.UserContext;
import com.campus.secondhand.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        return Result.ok(userService.login(dto));
    }

    @PostMapping("/register")
    public Result<User> register(@Valid @RequestBody RegisterDTO dto) {
        return Result.ok(userService.register(dto));
    }

    @GetMapping("/profile")
    public Result<User> profile() {
        return Result.ok(userService.getProfile(UserContext.getUserId()));
    }

    @PutMapping("/profile")
    public Result<User> updateProfile(@RequestBody User user) {
        return Result.ok(userService.updateProfile(UserContext.getUserId(), user));
    }
}
