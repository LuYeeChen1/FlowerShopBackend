package com.backend.flowershop.domain.model;

import java.time.LocalDateTime;

public class CartItem {
    private Long id;
    private String userId;
    private Long flowerId;
    private Integer quantity;
    private LocalDateTime createdAt;

    // --- 扩展字段 (用于前端展示，非数据库直接映射) ---
    // Repository 层在查询时会通过 JOIN flowers 表填充这些数据
    private String flowerName;
    private Double flowerPrice;
    private String flowerImageUrl;

    public CartItem() {}

    public CartItem(Long id, String userId, Long flowerId, Integer quantity) {
        this.id = id;
        this.userId = userId;
        this.flowerId = flowerId;
        this.quantity = quantity;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Long getFlowerId() { return flowerId; }
    public void setFlowerId(Long flowerId) { this.flowerId = flowerId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // --- 扩展字段 Getters & Setters ---
    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }

    public Double getFlowerPrice() { return flowerPrice; }
    public void setFlowerPrice(Double flowerPrice) { this.flowerPrice = flowerPrice; }

    public String getFlowerImageUrl() { return flowerImageUrl; }
    public void setFlowerImageUrl(String flowerImageUrl) { this.flowerImageUrl = flowerImageUrl; }
}