package com.campus.secondhand.security;

import java.lang.annotation.*;

/**
 * 角色权限注解。
 * 标注在 Controller 类或方法上，指定访问所需的角色。
 * 未标注 = 任何已登录用户可访问。
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireRole {
    /** 允许的角色代码列表，如 {"ADMIN"} 或 {"ADMIN", "CAMPUS_AMBASSADOR"} */
    String[] value() default {};
}
