package com.backend.flowershop.infrastructure.persistence.jdbc;

import com.backend.flowershop.application.port.SellerProfileRepository;
import com.backend.flowershop.domain.model.SellerProfile;
import com.backend.flowershop.infrastructure.persistence.jdbc.mapper.SellerProfileRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

/**
 * 作用：
 * - JDBC 卖家档案仓储实现
 *
 * 职责边界：
 * - 负责数据库读写
 * - 不负责业务规则
 *
 * 使用位置：
 * - 卖家入驻应用服务与规则
 */
@Repository
public class JdbcSellerProfileRepository implements SellerProfileRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SellerProfileRowMapper rowMapper;

    /**
     * 做什么：
     * - 创建 JDBC 仓储
     *
     * 输入：
     * - jdbcTemplate：JDBC 模板
     *
     * 输出：
     * - 仓储对象
     */
    public JdbcSellerProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new SellerProfileRowMapper();
    }

    /**
     * 做什么：
     * - 保存卖家档案
     *
     * 输入：
     * - profile：卖家档案
     *
     * 输出：
     * - 保存后的档案
     */
    @Override
    public SellerProfile save(SellerProfile profile) {
        String sql = "INSERT INTO seller_profiles (user_id, display_name, shop_name, description, created_at, updated_at) "
                + "VALUES (?, ?, ?, ?, NOW(), NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, profile.getUserId());
            statement.setString(2, profile.getDisplayName());
            statement.setString(3, profile.getShopName());
            statement.setString(4, profile.getDescription());
            return statement;
        }, keyHolder);
        Long id = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;
        return findById(id).orElse(profile);
    }

    /**
     * 做什么：
     * - 根据用户 ID 查询卖家档案
     *
     * 输入：
     * - userId：用户 ID
     *
     * 输出：
     * - 可选的档案
     */
    @Override
    public Optional<SellerProfile> findByUserId(String userId) {
        String sql = "SELECT * FROM seller_profiles WHERE user_id = ?";
        return jdbcTemplate.query(sql, rowMapper, userId).stream().findFirst();
    }

    /**
     * 做什么：
     * - 根据用户 ID 更新卖家档案
     *
     * 输入：
     * - profile：卖家档案
     *
     * 输出：
     * - 是否更新成功
     */
    @Override
    public boolean updateByUserId(SellerProfile profile) {
        String sql = "UPDATE seller_profiles SET display_name = ?, shop_name = ?, description = ?, updated_at = NOW() "
                + "WHERE user_id = ?";
        int rows = jdbcTemplate.update(sql,
                profile.getDisplayName(),
                profile.getShopName(),
                profile.getDescription(),
                profile.getUserId());
        return rows > 0;
    }

    /**
     * 做什么：
     * - 根据主键查询卖家档案
     *
     * 输入：
     * - id：主键
     *
     * 输出：
     * - 可选的档案
     */
    private Optional<SellerProfile> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        String sql = "SELECT * FROM seller_profiles WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }
}
