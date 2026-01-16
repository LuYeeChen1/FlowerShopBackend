package com.backend.flowershop.domain.repository;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import java.util.List;

public interface OrderRepository {
    // Buyer Methods
    Long saveOrder(Order order);
    void saveOrderItems(List<OrderItem> items);
    List<Order> findByUserId(String userId);
    Order findById(Long orderId, String userId);
    List<OrderItem> findOrderItemsByUserId(String userId);

    // Seller Methods (✅ 必须包含这俩)
    List<SellerOrderDTOResponse> findOrdersBySellerId(String sellerId);
    void updateStatus(Long orderId, String status);
}