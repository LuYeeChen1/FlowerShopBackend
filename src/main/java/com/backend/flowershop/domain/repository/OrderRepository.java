package com.backend.flowershop.domain.repository;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.domain.enums.OrderStatus;
import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    // Buyer Methods
    Long saveOrder(Order order);
    void saveOrderItems(List<OrderItem> items);
    List<Order> findByUserId(String userId);

    // 通用查询
    Optional<Order> findById(Long id);
    Order findById(Long orderId, String userId);
    List<OrderItem> findOrderItemsByUserId(String userId);

    // Seller Methods
    List<SellerOrderDTOResponse> findOrdersBySellerId(String sellerId);
    Optional<SellerOrderDTOResponse> findOrderByIdAndSellerId(Long orderId, String sellerId);

    // 状态更新
    void updateStatus(Long orderId, OrderStatus status);
    void updateItemsStatusBySeller(Long orderId, String sellerId, OrderStatus status);
    boolean isOrderFullyShipped(Long orderId);

    // 查询
    List<OrderItem> findOrderItemsByOrderIdAndSellerId(Long orderId, String sellerId);
    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    // ✅ [新增] 更新商家收入 (正数增加，负数扣减)
    void updateSellerRevenue(String sellerId, BigDecimal amount);
}