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

    // 买家隐藏单个订单
    void hideOrderForBuyer(Long orderId);

    // 买家清空所有历史 (仅限终态订单)
    void hideAllCompletedOrdersForBuyer(String userId);

    // Common Methods
    Optional<Order> findById(Long id);
    Order findById(Long orderId, String userId);
    List<OrderItem> findOrderItemsByUserId(String userId);

    // Seller Methods
    List<SellerOrderDTOResponse> findOrdersBySellerId(String sellerId);
    Optional<SellerOrderDTOResponse> findOrderByIdAndSellerId(Long orderId, String sellerId);

    // 卖家隐藏订单 (实际是隐藏属于该卖家的 Items)
    void hideOrderItemsForSeller(Long orderId, String sellerId);

    // Status & Logic
    void updateStatus(Long orderId, OrderStatus status);
    void updateItemsStatusBySeller(Long orderId, String sellerId, OrderStatus status);
    boolean isOrderFullyShipped(Long orderId);

    List<OrderItem> findOrderItemsByOrderIdAndSellerId(Long orderId, String sellerId);
    List<OrderItem> findOrderItemsByOrderId(Long orderId);

    // Revenue
    void updateSellerRevenue(String sellerId, BigDecimal amount);
}