package com.backend.flowershop.domain.model;

import com.backend.flowershop.domain.enums.OrderStatus; // 引入枚举

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long orderId;
    private Long flowerId;
    private String flowerName;
    private BigDecimal priceAtPurchase;
    private Integer quantity;
    private String imageUrl;

    // 状态字段 (用于混合订单管理，标记该商品是否已发货)
    private OrderStatus status;

    // --- Getters & Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getFlowerId() { return flowerId; }
    public void setFlowerId(Long flowerId) { this.flowerId = flowerId; }

    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }

    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(BigDecimal priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    //Status 的 Getter/Setter
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }
}