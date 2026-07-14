package com.campus.secondhand.security;

public class UserContext {
    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> STUDENT_ID = new ThreadLocal<>();
    private static final ThreadLocal<String> ROLE = new ThreadLocal<>();

    public static void setCurrentUser(Long userId, String studentId, String role) {
        USER_ID.set(userId);
        STUDENT_ID.set(studentId);
        ROLE.set(role);
    }

    public static Long getUserId() {
        return USER_ID.get();
    }

    public static String getStudentId() {
        return STUDENT_ID.get();
    }

    public static String getRole() {
        return ROLE.get();
    }

    public static boolean isAdmin() {
        return "admin".equals(ROLE.get());
    }

    public static void clear() {
        USER_ID.remove();
        STUDENT_ID.remove();
        ROLE.remove();
    }
}
