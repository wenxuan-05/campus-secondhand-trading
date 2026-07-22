package com.campus.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.dto.*;
import com.campus.secondhand.entity.BuyRequest;
import com.campus.secondhand.entity.Goods;
import com.campus.secondhand.entity.Review;
import com.campus.secondhand.entity.User;
import com.campus.secondhand.mapper.BuyRequestMapper;
import com.campus.secondhand.mapper.GoodsMapper;
import com.campus.secondhand.mapper.ReviewMapper;
import com.campus.secondhand.mapper.UserMapper;
import com.campus.secondhand.security.JwtTokenProvider;
import com.campus.secondhand.service.MailService;
import com.campus.secondhand.service.UserService;
import com.campus.secondhand.service.VerificationCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final VerificationCodeService verificationCodeService;
    private final MailService mailService;
    private final GoodsMapper goodsMapper;
    private final BuyRequestMapper buyRequestMapper;
    private final ReviewMapper reviewMapper;
    private final ObjectMapper objectMapper;

    @Override
    public Map<String, Object> login(LoginDTO dto) {
        String encrypted = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8));
        String account = dto.getStudentId();
        User user;
        if (account.contains("@")) {
            // 邮箱登录
            user = getOne(new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, account)
                    .eq(User::getPassword, encrypted));
        } else {
            // 学号登录
            user = getOne(new LambdaQueryWrapper<User>()
                    .eq(User::getStudentId, account)
                    .eq(User::getPassword, encrypted));
        }
        if (user == null) {
            throw new BusinessException("学号/邮箱或密码错误");
        }
        if (user.getStatus() == 0) {
            if (user.getBanExpireTime() != null) {
                if (user.getBanExpireTime().isBefore(LocalDateTime.now())) {
                    // 封禁已到期，自动解封
                    user.setStatus(1);
                    user.setCreditScore(60);
                    user.setCreditLevel("一般");
                    user.setBanExpireTime(null);
                    updateById(user);
                } else {
                    String time = user.getBanExpireTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    throw new BusinessException("账号因信用分低于60已被封禁，将于 " + time + " 自动解封恢复至60分");
                }
            } else {
                throw new BusinessException("账号已被禁用，请联系管理员");
            }
        }
        if (user.getStatus() != 1) {
            throw new BusinessException("账号状态异常，请联系管理员");
        }
        int tokenVersion = user.getTokenVersion() != null ? user.getTokenVersion() : 0;
        // admin 账号强制为管理员角色
        if ("admin".equals(user.getStudentId()) && !"ADMIN".equals(user.getRole())) {
            user.setRole("ADMIN");
            updateById(user);
        }
        String role = user.getRole() != null ? user.getRole() : "USER";
        String token = jwtTokenProvider.generateToken(user.getId(), user.getStudentId(),
                user.getEmail(), role, tokenVersion);
        return Map.of("token", token, "user", sanitize(user));
    }

    @Override
    public User register(RegisterDTO dto) {
        // 校验密码一致性
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException("两次密码输入不一致");
        }
        // 校验学号格式
        if (!dto.getStudentId().matches("^\\d{8}$")) {
            throw new BusinessException("学号必须为8位数字");
        }
        // Check duplicate student ID
        long count = count(new LambdaQueryWrapper<User>()
                .eq(User::getStudentId, dto.getStudentId()));
        if (count > 0) {
            throw new BusinessException("该学号已注册");
        }
        // 校验验证码
        verificationCodeService.verify(dto.getStudentId(), dto.getCode(), "register");

        User user = new User();
        user.setStudentId(dto.getStudentId());
        user.setNickname(dto.getNickname());
        user.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setSchoolName("西南财经大学");
        user.setEmail(dto.getStudentId() + "@smail.swufe.edu.cn");
        user.setCreditScore(80);
        user.setStatus(1);
        user.setRole("USER");
        user.setTokenVersion(0);
        save(user);
        return sanitize(user);
    }

    @Override
    public String sendCode(SendCodeDTO dto) {
        String studentId = dto.getStudentId();
        String type = dto.getType();

        // 学号格式校验
        if (!studentId.matches("^\\d{8}$")) {
            throw new BusinessException("学号必须为8位数字");
        }

        if ("register".equals(type)) {
            // 注册：检查学号是否已注册
            long count = count(new LambdaQueryWrapper<User>()
                    .eq(User::getStudentId, studentId));
            if (count > 0) {
                throw new BusinessException("该学号已注册");
            }
        } else if ("reset".equals(type)) {
            // 重置密码：检查学号是否存在
            long count = count(new LambdaQueryWrapper<User>()
                    .eq(User::getStudentId, studentId));
            if (count == 0) {
                throw new BusinessException("该学号未注册");
            }
        } else {
            throw new BusinessException("无效的验证码类型");
        }

        // 生成验证码并发送邮件
        String code = verificationCodeService.generate(studentId, type);
        mailService.sendVerificationCode(studentId, code);
        return "验证码已发送至 " + studentId + "@smail.swufe.edu.cn";
    }

    @Override
    public void resetPassword(ResetPasswordDTO dto) {
        // 学号格式校验
        if (!dto.getStudentId().matches("^\\d{8}$")) {
            throw new BusinessException("学号必须为8位数字");
        }
        // 检查学号是否存在
        User user = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getStudentId, dto.getStudentId()));
        if (user == null) {
            throw new BusinessException("该学号未注册");
        }
        // 校验验证码
        verificationCodeService.verify(dto.getStudentId(), dto.getCode(), "reset");
        // 更新密码
        user.setPassword(DigestUtils.md5DigestAsHex(dto.getNewPassword().getBytes(StandardCharsets.UTF_8)));
        // Token版本号+1，使旧Token失效
        int currentVersion = user.getTokenVersion() != null ? user.getTokenVersion() : 0;
        user.setTokenVersion(currentVersion + 1);
        updateById(user);
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

    @Override
    public int unbanExpiredUsers() {
        List<User> banned = list(new LambdaQueryWrapper<User>()
                .eq(User::getStatus, 0)
                .isNotNull(User::getBanExpireTime)
                .le(User::getBanExpireTime, LocalDateTime.now()));
        int count = 0;
        for (User u : banned) {
            u.setStatus(1);
            u.setCreditScore(60);
            u.setBanExpireTime(null);
            u.setCreditLevel("一般");
            updateById(u);
            count++;
        }
        return count;
    }

    @Override
    public UserProductVO getUserProducts(Long userId) {
        User user = getById(userId);
        if (user == null) throw new BusinessException("用户不存在");

        UserProductVO vo = new UserProductVO();
        vo.setUserId(userId);
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());

        // Get on-sale goods
        List<Goods> goodsList = goodsMapper.selectList(new LambdaQueryWrapper<Goods>()
                .eq(Goods::getUserId, userId)
                .eq(Goods::getStatus, 2) // 在售
                .orderByDesc(Goods::getCreatedAt));
        vo.setGoods(goodsList.stream().map(g -> {
            UserProductVO.ProductItem item = new UserProductVO.ProductItem();
            item.setId(g.getId());
            item.setTitle(g.getTitle());
            item.setPrice(g.getPrice().toString());
            item.setImages(g.getImages());
            item.setStatus(g.getStatus());
            item.setCreatedAt(g.getCreatedAt() != null ? g.getCreatedAt().toString() : "");
            return item;
        }).toList());

        // Get buy requests
        List<BuyRequest> brList = buyRequestMapper.selectList(new LambdaQueryWrapper<BuyRequest>()
                .eq(BuyRequest::getUserId, userId)
                .in(BuyRequest::getStatus, 1, 2) // 发布中 or 沟通中
                .orderByDesc(BuyRequest::getCreatedAt));
        vo.setBuyRequests(brList.stream().map(br -> {
            UserProductVO.BuyRequestItem item = new UserProductVO.BuyRequestItem();
            item.setId(br.getId());
            item.setTitle(br.getTitle());
            item.setBudget(br.getBudget().toString());
            item.setStatus(br.getStatus());
            item.setCreatedAt(br.getCreatedAt() != null ? br.getCreatedAt().toString() : "");
            return item;
        }).toList());

        return vo;
    }

    @Override
    public UserPublicProfileVO getUserPublicProfile(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserPublicProfileVO vo = new UserPublicProfileVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setSchoolName(user.getSchoolName());
        vo.setCreditScore(user.getCreditScore());
        vo.setCreditLevel(user.getCreditLevel());
        vo.setTradeCount(user.getTradeCount());
        vo.setGoodRate(user.getGoodRate());
        vo.setDormitory(user.getDormitory());
        vo.setGender(user.getGender());

        // 真实姓名脱敏：张三 → 张*，张三丰 → 张*丰
        String realName = user.getRealName();
        if (realName != null && realName.length() > 0) {
            if (realName.length() == 1) {
                vo.setRealName(realName);
            } else if (realName.length() == 2) {
                vo.setRealName(realName.charAt(0) + "*");
            } else {
                vo.setRealName(realName.charAt(0) + "*".repeat(realName.length() - 2) + realName.charAt(realName.length() - 1));
            }
        }

        // 获取最近10条收到的评价，并填充评价人信息
        Page<Review> page = new Page<>(1, 10);
        List<Review> reviewList = reviewMapper.selectPage(page,
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getRevieweeId, userId)
                        .orderByDesc(Review::getCreatedAt)).getRecords();

        List<ReviewVO> reviewVOs = reviewList.stream().map(r -> {
            ReviewVO rv = new ReviewVO();
            rv.setId(r.getId());
            rv.setOrderId(r.getOrderId());
            rv.setReviewerId(r.getReviewerId());
            rv.setRating(r.getRating());
            rv.setContent(r.getContent());
            rv.setIsAuto(r.getIsAuto());
            rv.setReviewType(r.getReviewType());
            rv.setCreatedAt(r.getCreatedAt());

            // 解析 tags 和 images JSON
            try {
                rv.setTags(r.getTags() != null ? objectMapper.readValue(r.getTags(), List.class) : List.of());
                rv.setImages(r.getImages() != null ? objectMapper.readValue(r.getImages(), List.class) : List.of());
            } catch (Exception e) {
                rv.setTags(List.of());
                rv.setImages(List.of());
            }

            // 填充评价人信息
            User reviewer = getById(r.getReviewerId());
            if (reviewer != null) {
                rv.setReviewerName(reviewer.getNickname());
                rv.setReviewerAvatar(reviewer.getAvatar());
            }
            return rv;
        }).toList();
        vo.setReviews(reviewVOs);

        return vo;
    }
}
