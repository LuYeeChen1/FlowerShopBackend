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
        List<CartItemDTOResponse> cartItems = cartService.getMyCart(userId);
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDTOResponse item : cartItems) {
            int rowsAffected = flowerRepository.reduceStock(item.flowerId(), item.quantity());
            if (rowsAffected == 0) throw new RuntimeException("Stock insufficient for flower: " + item.name());
            total = total.add(BigDecimal.valueOf(item.subtotal()));
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(total);
        order.setStatus(OrderStatus.PAID);
        order.setReceiverEmail(userEmail);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setShippingAddress(request.getShippingAddress());

        Long orderId = orderRepository.saveOrder(order);

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
        cartRepository.deleteAllByUserId(userId);

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

    // ✅ [新增] 补充状态更新方法
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        // 简单校验状态流转 (可选)
        if (order.getStatus() == OrderStatus.CANCELLED && newStatus != OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot change status of cancelled order");
        }
        orderRepository.updateStatus(orderId, newStatus);
    }

    @Transactional
    public void requestCancel(Long orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) throw new RuntimeException("Unauthorized");
        if (order.getStatus() != OrderStatus.PAID) throw new RuntimeException("Cannot cancel order in current state");

        orderRepository.updateStatus(orderId, OrderStatus.CANCELLATION_REQUESTED);
    }

    @Transactional
    public void deleteOrder(Long orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUserId().equals(userId)) throw new RuntimeException("Unauthorized");

        if (order.getStatus() != OrderStatus.DELIVERED && order.getStatus() != OrderStatus.CANCELLED) {
            throw new RuntimeException("Only completed or cancelled orders can be deleted from history.");
        }

        orderRepository.hideOrderForBuyer(orderId);
    }

    @Transactional
    public void clearOrderHistory(String userId) {
        orderRepository.hideAllCompletedOrdersForBuyer(userId);
    }
}