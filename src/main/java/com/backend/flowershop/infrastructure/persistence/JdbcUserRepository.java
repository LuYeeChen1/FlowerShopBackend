package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.enums.Role;
import com.backend.flowershop.domain.model.User;
import com.backend.flowershop.domain.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        String sql = """
            INSERT INTO users (id, email, username, avatar_url, role, is_active, last_login_at) 
            VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            ON DUPLICATE KEY UPDATE 
                email = VALUES(email), 
                username = VALUES(username),
                avatar_url = VALUES(avatar_url),
                role = VALUES(role),
                last_login_at = CURRENT_TIMESTAMP
        """;

        jdbcTemplate.update(sql,
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getAvatarUrl(),
                user.getRole().name(), // ✅ Enum -> String
                user.getActive()
        );
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT id, email, username, avatar_url, role, is_active FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper, id).stream().findFirst();
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        // 1. 安全读取 Role 字符串
        String roleStr = rs.getString("role");

        // 2. 🛡️ 防御性转换：默认为 CUSTOMER，防止 null 或 拼写错误导致崩溃
        Role roleEnum = Role.CUSTOMER;
        if (roleStr != null) {
            try {
                roleEnum = Role.valueOf(roleStr);
            } catch (IllegalArgumentException e) {
                // 如果数据库里有脏数据（比如 "invalid_role"），这里会捕获并保持默认值
                System.err.println("Database contains invalid role: " + roleStr);
            }
        }

        User user = new User(
                rs.getString("id"),
                rs.getString("email"),
                rs.getString("username"),
                roleEnum // ✅ 传入安全的 Enum
        );
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    };
}