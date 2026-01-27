package com.backend.flowershop.domain.model;

import java.time.LocalDateTime;

public class SellerProfile {
    // 对应数据库中的 user_id (PK/FK)
    private String userId;

    // 实名信息
    private String realName;
    private String idCardNumber;

    // 物理联系
    private String phoneNumber;
    private String businessAddress;

    // 状态: PENDING_REVIEW, APPROVED, REJECTED
    private String status;

    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getIdCardNumber() { return idCardNumber; }
    public void setIdCardNumber(String idCardNumber) { this.idCardNumber = idCardNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBusinessAddress() { return businessAddress; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}