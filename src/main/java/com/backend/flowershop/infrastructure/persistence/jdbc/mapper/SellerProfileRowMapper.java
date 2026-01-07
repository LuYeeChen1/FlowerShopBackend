package com.backend.flowershop.infrastructure.persistence.jdbc.mapper;

import com.backend.flowershop.domain.model.SellerProfile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * 作用：
 * - 将数据库行映射为卖家档案
 *
 * 职责边界：
 * - 负责 ResultSet 到领域模型转换
 * - 不负责业务逻辑
 *
 * 使用位置：
 * - JDBC 仓储实现
 */
public class SellerProfileRowMapper implements RowMapper<SellerProfile> {

    /**
     * 做什么：
     * - 将结果集转换为卖家档案
     *
     * 输入：
     * - rs：结果集
     * - rowNum：行号
     *
     * 输出：
     * - 卖家档案对象
     */
    @Override
    public SellerProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
        return SellerProfile.of(
                rs.getLong("id"),
                rs.getString("user_id"),
                rs.getString("display_name"),
                rs.getString("shop_name"),
                rs.getString("description"),
                createdAt,
                updatedAt
        );
    }
}
