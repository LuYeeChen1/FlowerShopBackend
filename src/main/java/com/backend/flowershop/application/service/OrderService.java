package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.CreateOrderRequestDTO;
import com.backend.flowershop.application.dto.response.CartItemDTOResponse;
import com.backend.flowershop.domain.enums.OrderStatus;
import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import com.backend.flowershop.domain.repository.CartRepository;
import com.backend.flowershop.domain.repository.FlowerRepository;
import com.backend.flowershop.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final FlowerRepository flowerRepository;

    // 移除 EmailAdapter 依赖
    public OrderService(OrderRepository orderRepository,
                        CartService cartService,
                        CartRepository cartRepository,
                        FlowerRepository flowerRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.flowerRepository = flowerRepository;
    }

    @Transactional
    public Long checkout(String userId, String userEmail, CreateOrderRequestDTO request) {
        // 1. 获取购物车
        List<CartItemDTOResponse> cartItems = cartService.getMyCart(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. 扣减库存 & 计算总价
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDTOResponse item : cartItems) {
            int rowsAffected = flowerRepository.reduceStock(item.flowerId(), item.quantity());
            if (rowsAffected == 0) {
                throw new RuntimeException("Stock insufficient for flower: " + item.name());
            }
            BigDecimal itemSubtotal = BigDecimal.valueOf(item.subtotal());
            total = total.add(itemSubtotal);
        }

        // 3. 创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(total);

        // 初始状态: PAID
        order.setStatus(OrderStatus.PAID);

        order.setReceiverEmail(userEmail); // 依然保存 Email 到数据库，但不发邮件
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setShippingAddress(request.getShippingAddress());

        Long orderId = orderRepository.saveOrder(order);

        // 4. 保存订单详情
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDTOResponse item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setFlowerId(item.flowerId());
            orderItem.setFlowerName(item.name());
            orderItem.setPriceAtPurchase(BigDecimal.valueOf(item.price()));
            orderItem.setQuantity(item.quantity());
            orderItems.add(orderItem);
        }
        orderRepository.saveOrderItems(orderItems);

        // 5. 清空购物车
        cartRepository.deleteAllByUserId(userId);

        // 移除邮件发送逻辑

        return orderId;
    }

    public List<Order> getUserOrders(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) return Collections.emptyList();

        List<OrderItem> allItems = orderRepository.findOrderItemsByUserId(userId);
        Map<Long, List<OrderItem>> itemsByOrderId = allItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));

        for (Order order : orders) {
            order.setItems(itemsByOrderId.getOrDefault(order.getId(), new ArrayList<>()));
        }
        return orders;
    }

    // --- 状态机流转 (保留) ---

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        validateStateTransition(order.getStatus(), newStatus);
        orderRepository.updateStatus(orderId, newStatus);
    }

    private void validateStateTransition(OrderStatus current, OrderStatus next) {
        if (current == OrderStatus.PAID && next == OrderStatus.SHIPPED) return;
        if (current == OrderStatus.SHIPPED && next == OrderStatus.DELIVERED) return;
        if (current == OrderStatus.PAID && next == OrderStatus.CANCELLATION_REQUESTED) return;
        if (current == OrderStatus.CANCELLATION_REQUESTED && (next == OrderStatus.CANCELLED || next == OrderStatus.PAID)) return;
        if (current == next) return;

        throw new RuntimeException("Invalid status transition from " + current + " to " + next);
    }

    @Transactional
    public void requestCancel(Long orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to order");
        }

        if (order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("Cannot cancel order in state: " + order.getStatus());
        }

        orderRepository.updateStatus(orderId, OrderStatus.CANCELLATION_REQUESTED);
    }
}