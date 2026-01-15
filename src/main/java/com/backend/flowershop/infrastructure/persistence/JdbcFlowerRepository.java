package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcFlowerRepository implements FlowerRepository {

    private final JdbcTemplate jdbcTemplate;

    // 🔥 统一配置 S3 基础 URL
    // 优先读取配置文件中的 aws.s3.base-url，如果没有则使用默认值
    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public JdbcFlowerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 1. 保存鲜花 (卖家上架)
    @Override
    public void save(String sellerId, FlowerDTORequest dto) {
        String sql = """
            INSERT INTO flowers (name, description, price, stock, image_url, category, seller_id, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;
        // 注意：这里存入数据库的 image_url 依然是相对路径 (Key)，例如 "flowers/..."
        // 这样设计是为了以后迁移 CDN 或 Bucket 时更灵活
        jdbcTemplate.update(sql,
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getStock(),
                dto.getImageUrl(),
                dto.getCategory(),
                sellerId
        );
    }

    // 2. 查询所有公开鲜花 (买家首页)
    @Override
    public List<Flower> findAllPublic() {
        String sql = "SELECT id, name, description, price, stock, image_url, category, seller_id FROM flowers";
        return jdbcTemplate.query(sql, flowerRowMapper);
    }

    // 3. 查询特定卖家的库存 (卖家中心)
    @Override
    public List<Flower> findAllBySellerId(String sellerId) {
        String sql = "SELECT id, name, description, price, stock, image_url, category, seller_id FROM flowers WHERE seller_id = ? ORDER BY created_at DESC";

        // 这里直接复用 flowerRowMapper，或者手动写映射逻辑
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Flower flower = new Flower();
            flower.setId(rs.getLong("id"));
            flower.setName(rs.getString("name"));
            flower.setDescription(rs.getString("description"));
            flower.setPrice(rs.getBigDecimal("price"));
            flower.setStock(rs.getInt("stock"));

            // 🔥 关键：拼接完整 URL
            String rawKey = rs.getString("image_url");
            if (rawKey != null && !rawKey.startsWith("http")) {
                flower.setImageUrl(s3BaseUrl + rawKey);
            } else {
                flower.setImageUrl(rawKey);
            }

            flower.setCategory(rs.getString("category"));
            flower.setSellerId(rs.getString("seller_id"));
            return flower;
        }, sellerId);
    }

    // 4. 查询商品详情 + 卖家档案 (详情页)
    public Optional<FlowerDetailDTOResponse> findDetailById(Long flowerId) {
        String sql = """
            SELECT 
                f.id, f.name, f.description, f.price, f.stock, f.image_url, f.category,
                u.id as seller_id, u.avatar_url,
                -- 动态获取卖家名称：如果是个人取 real_name，如果是企业取 company_name
                COALESCE(i.real_name, b.company_name) as seller_name,
                -- 动态获取卖家类型
                CASE WHEN i.user_id IS NOT NULL THEN 'INDIVIDUAL' ELSE 'BUSINESS' END as seller_type,
                -- 检查状态
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

            // 🔥 关键：拼接完整 URL
            String rawKey = rs.getString("image_url");
            if (rawKey != null && !rawKey.startsWith("http")) {
                dto.setImageUrl(s3BaseUrl + rawKey);
            } else {
                dto.setImageUrl(rawKey);
            }

            dto.setCategory(rs.getString("category"));
            dto.setSellerId(rs.getString("seller_id"));
            dto.setSellerName(rs.getString("seller_name"));
            dto.setSellerType(rs.getString("seller_type"));
            dto.setSellerAvatar(rs.getString("avatar_url"));
            // 只有 ACTIVE 状态才算 Verified
            dto.setVerified("ACTIVE".equals(rs.getString("seller_status")));

            return dto;
        }, flowerId).stream().findFirst();
    }

    // --- 通用 RowMapper (减少重复代码) ---
    private final RowMapper<Flower> flowerRowMapper = (rs, rowNum) -> {
        Flower flower = new Flower();
        flower.setId(rs.getLong("id"));
        flower.setName(rs.getString("name"));
        flower.setDescription(rs.getString("description"));
        flower.setPrice(rs.getBigDecimal("price"));
        flower.setStock(rs.getInt("stock"));

        // 🔥 自动拼接 URL
        String rawKey = rs.getString("image_url");
        if (rawKey != null && !rawKey.startsWith("http")) {
            flower.setImageUrl(s3BaseUrl + rawKey);
        } else {
            flower.setImageUrl(rawKey);
        }

        flower.setCategory(rs.getString("category"));
        flower.setSellerId(rs.getString("seller_id"));
        return flower;
    };
}