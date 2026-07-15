package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.LoginDTO;
import com.campus.secondhand.dto.RegisterDTO;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.security.JwtTokenProvider;
import com.campus.secondhand.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        String encrypted = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getStudentId, dto.getStudentId())
                .eq(User::getPassword, encrypted));
        if (user == null) {
            throw new BusinessException("学号或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        String token = jwtTokenProvider.generateToken(user.getId(), user.getStudentId());
        return Map.of("token", token, "user", sanitize(user));
    }

    @Override
    public User register(RegisterDTO dto) {
        // Check duplicate student ID
        long count = count(new LambdaQueryWrapper<User>()
                .eq(User::getStudentId, dto.getStudentId()));
        if (count > 0) {
            throw new BusinessException("该学号已注册");
        }
        User user = new User();
        user.setStudentId(dto.getStudentId());
        user.setNickname(dto.getNickname());
        user.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setSchoolName(dto.getSchoolName() != null ? dto.getSchoolName() : "");
        user.setPhone(dto.getPhone() != null ? dto.getPhone() : "");
        user.setCreditScore(100);
        user.setStatus(1);
        save(user);
        return sanitize(user);
    }

    @Override
    public User getProfile(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return sanitize(user);
    }

    @Override
    public User updateProfile(Long userId, User update) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (update.getNickname() != null) user.setNickname(update.getNickname());
        if (update.getAvatar() != null) user.setAvatar(update.getAvatar());
        if (update.getPhone() != null) user.setPhone(update.getPhone());
        if (update.getSchoolName() != null) user.setSchoolName(update.getSchoolName());
        updateById(user);
        return sanitize(user);
    }

    private User sanitize(User user) {
        user.setPassword(null);
        return user;
    }
}
