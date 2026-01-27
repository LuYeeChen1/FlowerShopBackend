package com.backend.flowershop.domain.enums;

public enum OrderStatus {
    PENDING_PAYMENT,       // 待支付
    PAID,                  // 已支付 (买家申请取消的起点)
    CANCELLATION_REQUESTED,// [新增] 取消/退款申请中 (锁定订单，阻止发货)
    SHIPPED,               // 已发货 (此时通常不可取消，需走售后流程)
    DELIVERED,             // 已送达
    CANCELLED              // [新增] 已取消 (终态，释放库存)
}