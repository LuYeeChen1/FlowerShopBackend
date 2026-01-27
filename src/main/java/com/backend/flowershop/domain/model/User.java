package com.backend.flowershop.domain.model;

import com.backend.flowershop.domain.enums.Role; // 👈 确保引入 Enum
import java.time.LocalDateTime;

public class User {
    private String id;       // 对应 Cognito "sub"
    private String email;
    private String username;
    private String avatarUrl;

    // ✅ 核心修复：类型从 String 更改为 Role 枚举
    private Role role;     // CUSTOMER, SELLER, ADMIN

    private Boolean isActive;

    // 无参构造
    public User() {}

    // 全参构造
    // ✅ 核心修复：构造函数参数类型改为 Role
    public User(String id, String email, String username, Role role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
        this.isActive = true; // 默认激活
    }

    // ✅ 核心修复：Getter 返回类型改为 Role
    public Role getRole() {
        return role;
    }

    // ✅ 核心修复：Setter 参数类型改为 Role
    public void setRole(Role role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}