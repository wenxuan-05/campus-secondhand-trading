package com.campus.secondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.secondhand.dto.*;
import com.campus.secondhand.entity.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> login(LoginDTO dto);
    User register(RegisterDTO dto);
    User getProfile(Long userId);
    User updateProfile(Long userId, User user);
    int unbanExpiredUsers();
    String sendCode(SendCodeDTO dto);
    void resetPassword(ResetPasswordDTO dto);
    UserProductVO getUserProducts(Long userId);
    UserPublicProfileVO getUserPublicProfile(Long userId);
}
