package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcFlowerRepository implements FlowerRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public JdbcFlowerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Flower flower) {
        if (flower.getId() == null) {
            String sql = """
                INSERT INTO flowers (name, description, price, stock, image_url, category, seller_id, created_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            """;
            jdbcTemplate.update(sql, flower.getName(), flower.getDescription(), flower.getPrice(), flower.getStock(), flower.getImageUrl(), flower.getCategory(), flower.getSellerId());
        } else {
            String sql = "UPDATE flowers SET name=?, description=?, price=?, stock=?, image_url=?, category=? WHERE id=?";
            jdbcTemplate.update(sql, flower.getName(), flower.getDescription(), flower.getPrice(), flower.getStock(), flower.getImageUrl(), flower.getCategory(), flower.getId());
        }
    }

    @Override
    public List<Flower> findAllPublic() {
        String sql = "SELECT id, name, description, price, stock, image_url, category, seller_id FROM flowers";
        return jdbcTemplate.query(sql, flowerRowMapper);
    }

    @Override
    public List<Flower> findAllBySellerId(String sellerId) {
        String sql = "SELECT id, name, description, price, stock, image_url, category, seller_id FROM flowers WHERE seller_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, flowerRowMapper, sellerId);
    }

    @Override
    public Flower findById(Long id) {
        String sql = "SELECT id, name, description, price, stock, image_url, category, seller_id FROM flowers WHERE id = ?";
        return jdbcTemplate.query(sql, flowerRowMapper, id).stream().findFirst().orElse(null);
    }

    @Override
    public int reduceStock(Long flowerId, int quantity) {
        String sql = "UPDATE flowers SET stock = stock - ? WHERE id = ? AND stock >= ?";
        return jdbcTemplate.update(sql, quantity, flowerId, quantity);
    }

    // [新增] 恢复库存：当订单被取消时调用
    @Override
    public void restoreStock(Long flowerId, int quantity) {
        String sql = "UPDATE flowers SET stock = stock + ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, flowerId);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM flowers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Flower> findAllActive(String category, String search, int limit, int offset) {
        StringBuilder sql = new StringBuilder("SELECT * FROM flowers WHERE stock > 0");
        List<Object> params = new ArrayList<>();

        if (category != null && !category.isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            sql.append(" AND category = ?");
            params.add(category);
        }

        if (search != null && !search.isEmpty()) {
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
        }

        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), flowerRowMapper, params.toArray());
    }

    @Override
    public int countActive(String category, String search) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM flowers WHERE stock > 0");
        List<Object> params = new ArrayList<>();

        if (category != null && !category.isEmpty() && !"ALL".equalsIgnoreCase(category)) {
            sql.append(" AND category = ?");
            params.add(category);
        }

        if (search != null && !search.isEmpty()) {
            sql.append(" AND (name LIKE ? OR description LIKE ?)");
            params.add("%" + search + "%");
            params.add("%" + search + "%");
        }

        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
    }

    @Override
    public Optional<FlowerDetailDTOResponse> findDetailById(Long flowerId) {
        // ... (保持原有的 findDetailById 代码不变)
        String sql = """
            SELECT f.id, f.name, f.description, f.price, f.stock, f.image_url, f.category,
                   u.id as seller_id, u.avatar_url,
                   COALESCE(i.real_name, b.company_name) as seller_name,
                   CASE WHEN i.user_id IS NOT NULL THEN 'INDIVIDUAL' ELSE 'BUSINESS' END as seller_type,
                   COALESCE(i.status, b.status) as seller_status
            FROM flowers f
            JOIN users u ON f.seller_id = u.id
            LEFT JOIN individual_sellers i ON u.id = i.user_id
            LEFT JOIN business_sellers b ON u.id = b.user_id
            WHERE f.id = ?
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            FlowerDetailDTOResponse dto = new FlowerDetailDTOResponse();
            dto.setId(rs.getLong("id"));
            dto.setName(rs.getString("name"));
            dto.setDescription(rs.getString("description"));
            dto.setPrice(rs.getBigDecimal("price"));
            dto.setStock(rs.getInt("stock"));
            String rawKey = rs.getString("image_url");
            if (rawKey != null && !rawKey.startsWith("http")) dto.setImageUrl(s3BaseUrl + rawKey);
            else dto.setImageUrl(rawKey);
            dto.setCategory(rs.getString("category"));
            dto.setSellerId(rs.getString("seller_id"));
            dto.setSellerName(rs.getString("seller_name"));
            dto.setSellerType(rs.getString("seller_type"));
            dto.setSellerAvatar(rs.getString("avatar_url"));
            dto.setVerified("ACTIVE".equals(rs.getString("seller_status")));
            return dto;
        }, flowerId).stream().findFirst();
    }

    private final RowMapper<Flower> flowerRowMapper = (rs, rowNum) -> {
        Flower flower = new Flower();
        flower.setId(rs.getLong("id"));
        flower.setName(rs.getString("name"));
        flower.setDescription(rs.getString("description"));
        flower.setPrice(rs.getBigDecimal("price"));
        flower.setStock(rs.getInt("stock"));
        String rawKey = rs.getString("image_url");
        if (rawKey != null && !rawKey.startsWith("http")) flower.setImageUrl(s3BaseUrl + rawKey);
        else flower.setImageUrl(rawKey);
        flower.setCategory(rs.getString("category"));
        flower.setSellerId(rs.getString("seller_id"));
        return flower;
    };
}