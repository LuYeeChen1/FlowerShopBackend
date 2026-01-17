package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.domain.enums.OrderStatus;
import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import com.backend.flowershop.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveOrder(Order order) {
        String sql = """
            INSERT INTO orders 
            (user_id, total_price, status, shipping_address, receiver_name, receiver_phone, receiver_email, created_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;
        jdbcTemplate.update(sql,
                order.getUserId(),
                order.getTotalPrice(),
                order.getStatus().name(),
                order.getShippingAddress(),
                order.getReceiverName(),
                order.getReceiverPhone(),
                order.getReceiverEmail()
        );
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    @Override
    public void saveOrderItems(List<OrderItem> items) {
        String sql = "INSERT INTO order_items (order_id, flower_id, flower_name, price_at_purchase, quantity, status) VALUES (?, ?, ?, ?, ?, ?)";

        for (OrderItem item : items) {
            String status = OrderStatus.PAID.name();
            jdbcTemplate.update(sql, item.getOrderId(), item.getFlowerId(), item.getFlowerName(), item.getPriceAtPurchase(), item.getQuantity(), status);
        }
    }

    @Override
    public List<Order> findByUserId(String userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, orderRowMapper, userId);
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, id).stream().findFirst();
    }

    @Override
    public Order findById(Long orderId, String userId) {
        String sql = "SELECT * FROM orders WHERE id = ? AND user_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, orderId, userId).stream().findFirst().orElse(null);
    }

    @Override
    public List<OrderItem> findOrderItemsByUserId(String userId) {
        String sql = """
            SELECT oi.*, f.image_url 
            FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            LEFT JOIN flowers f ON oi.flower_id = f.id
            WHERE o.user_id = ?
            ORDER BY oi.order_id DESC, oi.id ASC
        """;
        return jdbcTemplate.query(sql, orderItemRowMapper, userId);
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status.name(), orderId);
    }

    @Override
    public void updateItemsStatusBySeller(Long orderId, String sellerId, OrderStatus status) {
        String sql = """
            UPDATE order_items oi
            JOIN flowers f ON oi.flower_id = f.id
            SET oi.status = ?
            WHERE oi.order_id = ? AND f.seller_id = ?
        """;
        jdbcTemplate.update(sql, status.name(), orderId, sellerId);
    }

    @Override
    public boolean isOrderFullyShipped(Long orderId) {
        String sql = """
            SELECT COUNT(*) FROM order_items 
            WHERE order_id = ? 
            AND status NOT IN ('SHIPPED', 'DELIVERED')
        """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, orderId);
        return count != null && count == 0;
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderIdAndSellerId(Long orderId, String sellerId) {
        String sql = """
            SELECT oi.*, f.image_url 
            FROM order_items oi
            JOIN flowers f ON oi.flower_id = f.id
            WHERE oi.order_id = ? AND f.seller_id = ?
        """;
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId, sellerId);
    }

    @Override
    public List<OrderItem> findOrderItemsByOrderId(Long orderId) {
        String sql = """
            SELECT oi.*, f.image_url 
            FROM order_items oi
            LEFT JOIN flowers f ON oi.flower_id = f.id
            WHERE oi.order_id = ?
        """;
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }

    // ✅ [新增] 核心实现：更新商家钱包
    @Override
    public void updateSellerRevenue(String sellerId, BigDecimal amount) {
        // 如果钱包记录不存在则插入，存在则更新余额
        String sql = """
            INSERT INTO seller_wallets (seller_id, total_revenue) 
            VALUES (?, ?) 
            ON DUPLICATE KEY UPDATE total_revenue = total_revenue + ?
        """;
        jdbcTemplate.update(sql, sellerId, amount, amount);
    }

    @Override
    public Optional<SellerOrderDTOResponse> findOrderByIdAndSellerId(Long orderId, String sellerId) {
        String sqlOrder = """
            SELECT DISTINCT o.id, o.created_at, o.receiver_name, o.receiver_phone, o.receiver_email, o.shipping_address, o.total_price, o.status
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            JOIN flowers f ON oi.flower_id = f.id
            WHERE o.id = ? AND f.seller_id = ?
        """;

        return jdbcTemplate.query(sqlOrder, (rs, rowNum) -> {
            Long oId = rs.getLong("id");
            String sqlItems = """
                SELECT oi.flower_name, oi.quantity, oi.price_at_purchase, f.image_url
                FROM order_items oi
                JOIN flowers f ON oi.flower_id = f.id
                WHERE oi.order_id = ? AND f.seller_id = ?
            """;

            List<SellerOrderDTOResponse.SellerOrderItemDTO> items = jdbcTemplate.query(sqlItems, (rs2, rn2) -> {
                String rawKey = rs2.getString("image_url");
                String fullUrl = (rawKey != null && !rawKey.startsWith("http")) ? s3BaseUrl + rawKey : rawKey;
                return new SellerOrderDTOResponse.SellerOrderItemDTO(
                        rs2.getString("flower_name"),
                        rs2.getInt("quantity"),
                        rs2.getBigDecimal("price_at_purchase"),
                        fullUrl
                );
            }, oId, sellerId);

            return new SellerOrderDTOResponse(
                    oId,
                    rs.getString("receiver_name"),
                    rs.getString("receiver_phone"),
                    rs.getString("receiver_email"),
                    rs.getString("shipping_address"),
                    rs.getBigDecimal("total_price"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    items
            );
        }, orderId, sellerId).stream().findFirst();
    }

    @Override
    public List<SellerOrderDTOResponse> findOrdersBySellerId(String sellerId) {
        String sqlIds = """
            SELECT DISTINCT o.id, o.created_at
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            JOIN flowers f ON oi.flower_id = f.id
            WHERE f.seller_id = ?
            ORDER BY o.created_at DESC
        """;
        List<Long> orderIds = jdbcTemplate.query(sqlIds, (rs, rowNum) -> rs.getLong("id"), sellerId);

        return orderIds.stream().map(orderId -> {
            String sqlOrder = "SELECT * FROM orders WHERE id = ?";
            return jdbcTemplate.query(sqlOrder, (rs, rowNum) -> {
                Long oId = rs.getLong("id");
                String sqlItems = """
                    SELECT oi.flower_name, oi.quantity, oi.price_at_purchase, f.image_url
                    FROM order_items oi
                    JOIN flowers f ON oi.flower_id = f.id
                    WHERE oi.order_id = ? AND f.seller_id = ?
                """;

                List<SellerOrderDTOResponse.SellerOrderItemDTO> items = jdbcTemplate.query(sqlItems, (rs2, rn2) -> {
                    String rawKey = rs2.getString("image_url");
                    String fullUrl = (rawKey != null && !rawKey.startsWith("http")) ? s3BaseUrl + rawKey : rawKey;
                    return new SellerOrderDTOResponse.SellerOrderItemDTO(
                            rs2.getString("flower_name"),
                            rs2.getInt("quantity"),
                            rs2.getBigDecimal("price_at_purchase"),
                            fullUrl
                    );
                }, oId, sellerId);

                return new SellerOrderDTOResponse(
                        oId,
                        rs.getString("receiver_name"),
                        rs.getString("receiver_phone"),
                        rs.getString("receiver_email"),
                        rs.getString("shipping_address"),
                        rs.getBigDecimal("total_price"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        items
                );
            }, orderId).stream().findFirst().orElse(null);
        }).toList();
    }

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getString("user_id"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        try {
            String statusStr = rs.getString("status");
            if (statusStr != null) order.setStatus(OrderStatus.valueOf(statusStr));
            else order.setStatus(OrderStatus.PENDING_PAYMENT);
        } catch (IllegalArgumentException e) {
            order.setStatus(OrderStatus.PENDING_PAYMENT);
        }
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setReceiverName(rs.getString("receiver_name"));
        order.setReceiverPhone(rs.getString("receiver_phone"));
        order.setReceiverEmail(rs.getString("receiver_email"));
        return order;
    };

    private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setFlowerId(rs.getLong("flower_id"));
        item.setFlowerName(rs.getString("flower_name"));
        item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
        item.setQuantity(rs.getInt("quantity"));
        try {
            String statusStr = rs.getString("status");
            if (statusStr != null) item.setStatus(OrderStatus.valueOf(statusStr));
        } catch (Exception e) { }

        String rawKey = rs.getString("image_url");
        if (rawKey != null) {
            item.setImageUrl(rawKey.startsWith("http") ? rawKey : s3BaseUrl + rawKey);
        }
        return item;
    };
}