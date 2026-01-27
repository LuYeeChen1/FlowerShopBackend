package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.CartItem;
import com.backend.flowershop.domain.repository.CartRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcCartRepository implements CartRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCartRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addToCart(String userId, Long flowerId, int quantity) {
        // 🔥 核心 SQL：如果存在则更新数量，不存在则插入
        String sql = """
            INSERT INTO cart_items (user_id, flower_id, quantity)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE 
            quantity = quantity + VALUES(quantity),
            updated_at = CURRENT_TIMESTAMP
        """;
        jdbcTemplate.update(sql, userId, flowerId, quantity);
    }

    @Override
    public List<CartItem> findAllByUserId(String userId) {
        // 🔥 关联查询：同时取出购物车数据和鲜花详情
        String sql = """
            SELECT 
                c.id, c.user_id, c.flower_id, c.quantity, c.created_at,
                f.name as flower_name,
                f.price as flower_price,
                f.image_url as flower_image_url
            FROM cart_items c
            INNER JOIN flowers f ON c.flower_id = f.id
            WHERE c.user_id = ?
            ORDER BY c.created_at DESC
        """;
        return jdbcTemplate.query(sql, cartItemRowMapper, userId);
    }

    @Override
    public void deleteCartItem(Long cartId, String userId) {
        // 安全删除：确保只能删除属于自己的购物车条目
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        jdbcTemplate.update(sql, cartId, userId);
    }

    @Override
    public void clearCart(String userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    @Override
    public void updateQuantity(Long cartId, String userId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ? AND user_id = ?";
        jdbcTemplate.update(sql, quantity, cartId, userId);
    }

    @Override
    public void deleteAllByUserId(String userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    // --- RowMapper: 将 SQL 结果映射为 Java 对象 ---
    private final RowMapper<CartItem> cartItemRowMapper = (rs, rowNum) -> {
        CartItem item = new CartItem();
        // 1. 基础字段
        item.setId(rs.getLong("id"));
        item.setUserId(rs.getString("user_id"));
        item.setFlowerId(rs.getLong("flower_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // 2. 扩展字段 (来自 flowers 表)
        item.setFlowerName(rs.getString("flower_name"));
        item.setFlowerPrice(rs.getDouble("flower_price"));
        item.setFlowerImageUrl(rs.getString("flower_image_url"));

        return item;
    };
}