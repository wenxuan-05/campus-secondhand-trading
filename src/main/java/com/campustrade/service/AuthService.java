package com.campustrade.service;

import com.campustrade.dto.*;
import com.campustrade.entity.User;
import com.campustrade.entity.UserAuth;
import com.campustrade.entity.StudentInfo;
import com.campustrade.mapper.UserMapper;
import com.campustrade.mapper.UserAuthMapper;
import com.campustrade.mapper.StudentInfoMapper;
import com.campustrade.util.JwtUtil;
import com.campustrade.util.PasswordUtil;
import com.campustrade.util.UserNoGenerator;
import com.campustrade.vo.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final UserAuthMapper userAuthMapper;
    private final StudentInfoMapper studentInfoMapper;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;
    private final UserNoGenerator userNoGenerator;

    @Transactional
    public Result<AuthResponse> register(RegisterRequest request) {
        User existUser = userMapper.selectByPhone(request.getPhone());
        if (existUser != null) {
            return Result.error(1001, "该手机号已注册");
        }

        User user = new User();
        user.setUserNo(userNoGenerator.generate());
        user.setPhone(request.getPhone());
        user.setPassword(passwordUtil.encode(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : "用户" + request.getPhone().substring(7));
        user.setStatus(1);
        user.setCreditScore(100);
        user.setCreditLevel(1);
        user.setTradeCount(0);
        user.setGoodRate(100.0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userMapper.insert(user);

        UserAuth auth = new UserAuth();
        auth.setUserId(user.getId());
        auth.setAuthType(1);
        auth.setAuthId("");
        auth.setStatus(0);
        auth.setCreatedAt(LocalDateTime.now());
        auth.setUpdatedAt(LocalDateTime.now());
        userAuthMapper.insert(auth);

        String accessToken = jwtUtil.generateToken(user.getId(), user.getUserNo());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        log.info("用户注册成功: phone={}, userNo={}", request.getPhone(), user.getUserNo());

        return Result.success(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(86400L)
                .userInfo(buildUserProfile(user, null))
                .build());
    }

    public Result<AuthResponse> login(LoginRequest request) {
        User user = userMapper.selectByPhone(request.getPhone());
        if (user == null) {
            return Result.error(1001, "手机号或密码错误");
        }

        if (user.getStatus() == 0) {
            return Result.error(1002, "账号已被禁用");
        }

        if (!passwordUtil.matches(request.getPassword(), user.getPassword())) {
            return Result.error(1001, "手机号或密码错误");
        }

        user.setLastLoginTime(LocalDateTime.now());
        userMapper.updateById(user);

        UserAuth auth = userAuthMapper.selectByUserAndType(user.getId(), 1);

        String accessToken = jwtUtil.generateToken(user.getId(), user.getUserNo());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        log.info("用户登录成功: phone={}, userNo={}", request.getPhone(), user.getUserNo());

        return Result.success(AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(86400L)
                .userInfo(buildUserProfile(user, auth))
                .build());
    }

    @Transactional
    public Result<UserProfileDTO> verifyStudent(Long userId, StudentAuthRequest request) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        UserAuth existAuth = userAuthMapper.selectByUserAndType(userId, 1);
        if (existAuth != null && existAuth.getStatus() == 2) {
            return Result.error(1003, "该用户已完成学号认证");
        }

        StudentInfo existStudent = studentInfoMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<StudentInfo>()
                        .eq(StudentInfo::getStudentId, request.getStudentId())
        );
        if (existStudent != null && !existStudent.getUserId().equals(userId)) {
            return Result.error(1004, "该学号已被其他账号绑定");
        }

        boolean authSuccess = mockSchoolAuth(request.getStudentId(), request.getRealName(), request.getIdCardSuffix());

        if (!authSuccess) {
            if (existAuth != null) {
                existAuth.setStatus(3);
                userAuthMapper.updateById(existAuth);
            }
            return Result.error(1005, "学号认证失败，请检查信息是否正确");
        }

        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setUserId(userId);
        studentInfo.setStudentId(request.getStudentId());
        studentInfo.setRealName(request.getRealName());
        studentInfo.setSchoolId(request.getSchoolId() != null ? request.getSchoolId() : "S001");
        studentInfo.setSchoolName("某某大学");
        studentInfo.setCampus(request.getCampus());
        studentInfo.setDormitory(request.getDormitory());
        studentInfo.setRoomNumber(request.getRoomNumber());
        studentInfo.setGender(request.getGender());
        studentInfo.setIdCardSuffix(request.getIdCardSuffix());
        studentInfoMapper.insert(studentInfo);

        if (existAuth == null) {
            existAuth = new UserAuth();
            existAuth.setUserId(userId);
            existAuth.setAuthType(1);
        }
        existAuth.setAuthId(request.getStudentId());
        existAuth.setAuthName(request.getRealName());
        existAuth.setStatus(2);
        existAuth.setVerifiedAt(LocalDateTime.now());
        userAuthMapper.updateById(existAuth);

        log.info("学号认证成功: userId={}, studentId={}", userId, request.getStudentId());

        return Result.success(buildUserProfile(user, existAuth));
    }

    public Result<UserProfileDTO> getProfile(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }

        UserAuth auth = userAuthMapper.selectByUserAndType(userId, 1);
        return Result.success(buildUserProfile(user, auth));
    }

    public Result<AuthResponse> refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            return Result.error(401, "Refresh Token无效");
        }

        Long userId = jwtUtil.getUserId(refreshToken);
        User user = userMapper.selectById(userId);

        if (user == null || user.getStatus() == 0) {
            return Result.error(401, "用户不存在或已被禁用");
        }

        String newAccessToken = jwtUtil.generateToken(userId, user.getUserNo());
        String newRefreshToken = jwtUtil.generateRefreshToken(userId);

        return Result.success(AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .expiresIn(86400L)
                .build());
    }

    private boolean mockSchoolAuth(String studentId, String realName, String idCardSuffix) {
        return studentId != null && studentId.length() >= 8
                && realName != null && realName.length() >= 2
                && idCardSuffix != null && idCardSuffix.matches("\\d{4}");
    }

    private UserProfileDTO buildUserProfile(User user, UserAuth auth) {
        UserProfileDTO.UserProfileDTOBuilder builder = UserProfileDTO.builder()
                .id(user.getId())
                .userNo(user.getUserNo())
                .phone(user.getPhone())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .status(user.getStatus())
                .creditScore(user.getCreditScore())
                .creditLevel(convertCreditLevel(user.getCreditLevel()))
                .tradeCount(user.getTradeCount())
                .goodRate(user.getGoodRate())
                .isVerified(auth != null && auth.getStatus() == 2);

        if (auth != null && auth.getStatus() == 2) {
            StudentInfo student = studentInfoMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<StudentInfo>()
                            .eq(StudentInfo::getUserId, user.getId())
            );
            if (student != null) {
                builder.studentId(student.getStudentId())
                        .realName(student.getRealName())
                        .schoolName(student.getSchoolName())
                        .campus(student.getCampus())
                        .dormitory(student.getDormitory());
            }
        }

        return builder.build();
    }

    private String convertCreditLevel(Integer level) {
        return switch (level) {
            case 1 -> "优秀";
            case 2 -> "良好";
            case 3 -> "一般";
            case 4 -> "较差";
            case 5 -> "极差";
            default -> "良好";
        };
    }
}