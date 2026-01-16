package com.backend.flowershop.application.dto.request;

public class CreateOrderRequestDTO {
    private String receiverName;    //  新增：收货人姓名
    private String receiverPhone;   //  新增：联系电话
    private String shippingAddress; // 仅地址 (如: 123 Jalan Ampang, KL)

    // Getters & Setters
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getReceiverPhone() { return receiverPhone; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}