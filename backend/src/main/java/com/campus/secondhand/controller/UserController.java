package com.campus.secondhand.controller;

import com.campus.secondhand.common.Result;
import com.campus.secondhand.dto.*;
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

    @PostMapping("/send-code")
    public Result<String> sendCode(@Valid @RequestBody SendCodeDTO dto) {
        return Result.ok(userService.sendCode(dto));
    }

    @PostMapping("/reset-password")
    public Result<Void> resetPassword(@Valid @RequestBody ResetPasswordDTO dto) {
        userService.resetPassword(dto);
        return Result.ok();
    }

    @GetMapping("/profile")
    public Result<User> profile() {
        return Result.ok(userService.getProfile(UserContext.getUserId()));
    }

    @PutMapping("/profile")
    public Result<User> updateProfile(@RequestBody User user) {
        return Result.ok(userService.updateProfile(UserContext.getUserId(), user));
    }

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.fail(404, "用户不存在");
        }
        user.setPassword(null);
        return Result.ok(user);
    }

    @GetMapping("/{userId}/products")
    public Result<UserProductVO> userProducts(@PathVariable Long userId) {
        return Result.ok(userService.getUserProducts(userId));
    }

    @GetMapping("/{id}/public-profile")
    public Result<UserPublicProfileVO> getUserPublicProfile(@PathVariable Long id) {
        return Result.ok(userService.getUserPublicProfile(id));
    }
}
