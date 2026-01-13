package com.backend.flowershop.application.dto.request;

// 用于接收前端提交的表单数据
public record SellerApplyDTORequest(
        String realName,
        String idCardNumber,
        String phoneNumber,
        String businessAddress
) {}