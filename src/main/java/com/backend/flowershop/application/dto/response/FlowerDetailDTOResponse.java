package com.backend.flowershop.application.dto.response;

import java.math.BigDecimal;

public class FlowerDetailDTOResponse {
    // 鲜花基础信息
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl; // 完整的 S3 URL
    private String category;

    // 卖家公开信息
    private String sellerId;
    private String sellerName; // 个人实名 或 公司名
    private String sellerType; // INDIVIDUAL 或 BUSINESS
    private String sellerAvatar;
    private boolean isVerified; // 是否已认证 (Active)

    // Constructors, Getters, Setters
    public FlowerDetailDTOResponse() {}

    public FlowerDetailDTOResponse(Long id, String name, String description, BigDecimal price, Integer stock, String imageUrl, String category, String sellerId, String sellerName, String sellerType, String sellerAvatar, boolean isVerified) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.category = category;
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.sellerType = sellerType;
        this.sellerAvatar = sellerAvatar;
        this.isVerified = isVerified;
    }

    // 省略 Getter/Setter 以节省篇幅，请务必生成
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public String getSellerType() { return sellerType; }
    public void setSellerType(String sellerType) { this.sellerType = sellerType; }
    public String getSellerAvatar() { return sellerAvatar; }
    public void setSellerAvatar(String sellerAvatar) { this.sellerAvatar = sellerAvatar; }
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }
}