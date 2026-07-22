package com.campus.secondhand.common;

import lombok.Getter;

@Getter
public enum RoleEnum {
    USER("USER", "普通用户"),
    CAMPUS_AMBASSADOR("CAMPUS_AMBASSADOR", "校园大使"),
    ADMIN("ADMIN", "管理员");

    private final String code;
    private final String label;

    RoleEnum(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public static RoleEnum fromCode(String code) {
        for (RoleEnum r : values()) {
            if (r.code.equals(code)) return r;
        }
        return USER;
    }
}
