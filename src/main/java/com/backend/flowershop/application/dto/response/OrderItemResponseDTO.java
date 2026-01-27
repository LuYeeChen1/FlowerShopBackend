package com.backend.flowershop.application.dto.response;

import java.math.BigDecimal;

public class OrderItemResponseDTO {
    private String flowerName;
    private BigDecimal priceAtPurchase;
    private int quantity;

    // Getters and Setters ...
    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }
    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(BigDecimal priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}