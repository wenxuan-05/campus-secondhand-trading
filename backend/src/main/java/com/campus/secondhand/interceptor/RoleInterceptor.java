package com.campus.secondhand.interceptor;

import com.campus.secondhand.common.BusinessException;
import com.campus.secondhand.security.RequireRole;
import com.campus.secondhand.security.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        if (!(handler instanceof HandlerMethod hm)) return true;

        // 先检查方法级注解，再检查类级注解
        RequireRole annotation = hm.getMethodAnnotation(RequireRole.class);
        if (annotation == null) {
            annotation = hm.getBeanType().getAnnotation(RequireRole.class);
        }
        if (annotation == null) return true; // 无角色限制，放行

        String currentRole = UserContext.getRole();
        if (!StringUtils.hasText(currentRole)) {
            throw new BusinessException(403, "未授权，请先登录");
        }

        List<String> allowed = Arrays.asList(annotation.value());
        if (allowed.isEmpty() || allowed.contains(currentRole)) {
            return true;
        }

        throw new BusinessException(403, "权限不足：需要角色 " + String.join(" 或 ", allowed));
    }
}
