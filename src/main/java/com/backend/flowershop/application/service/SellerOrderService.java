package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.domain.enums.OrderStatus;
import com.backend.flowershop.domain.model.OrderItem;
import com.backend.flowershop.domain.repository.FlowerRepository;
import com.backend.flowershop.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SellerOrderService {

    private final OrderRepository orderRepository;
    private final FlowerRepository flowerRepository;

    public SellerOrderService(OrderRepository orderRepository, FlowerRepository flowerRepository) {
        this.orderRepository = orderRepository;
        this.flowerRepository = flowerRepository;
    }

    public List<SellerOrderDTOResponse> getIncomingOrders(String sellerId) {
        return orderRepository.findOrdersBySellerId(sellerId);
    }

    public SellerOrderDTOResponse getOrderDetails(Long orderId, String sellerId) {
        return orderRepository.findOrderByIdAndSellerId(orderId, sellerId)
                .orElseThrow(() -> new RuntimeException("Order not found or you do not have permission to view it."));
    }

    @Transactional
    public void shipOrder(Long orderId, String sellerId) {
        orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.SHIPPED);
        boolean isFullyShipped = orderRepository.isOrderFullyShipped(orderId);
        if (isFullyShipped) {
            orderRepository.updateStatus(orderId, OrderStatus.SHIPPED);
        }
    }

    @Transactional
    public void deliverOrder(Long orderId, String sellerId) {
        orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.DELIVERED);
        List<OrderItem> allItems = orderRepository.findOrderItemsByOrderId(orderId);
        boolean allDelivered = allItems.stream().allMatch(item -> item.getStatus() == OrderStatus.DELIVERED);
        if (allDelivered) {
            orderRepository.updateStatus(orderId, OrderStatus.DELIVERED);
        }
    }

    @Transactional
    public void auditCancellation(Long orderId, String sellerId, boolean approved) {
        if (approved) {
            orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);
            orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.CANCELLED);

            List<OrderItem> items = orderRepository.findOrderItemsByOrderIdAndSellerId(orderId, sellerId);
            BigDecimal refundAmount = BigDecimal.ZERO;

            for (OrderItem item : items) {
                flowerRepository.restoreStock(item.getFlowerId(), item.getQuantity());
                BigDecimal itemTotal = item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity()));
                refundAmount = refundAmount.add(itemTotal);
            }

            if (refundAmount.compareTo(BigDecimal.ZERO) > 0) {
                orderRepository.updateSellerRevenue(sellerId, refundAmount.negate());
            }
        } else {
            orderRepository.updateStatus(orderId, OrderStatus.PAID);
        }
    }

    @Transactional
    public void forceCancel(Long orderId, String sellerId) {
        orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);
        orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.CANCELLED);

        List<OrderItem> items = orderRepository.findOrderItemsByOrderIdAndSellerId(orderId, sellerId);
        BigDecimal refundAmount = BigDecimal.ZERO;

        for (OrderItem item : items) {
            flowerRepository.restoreStock(item.getFlowerId(), item.getQuantity());
            BigDecimal itemTotal = item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity()));
            refundAmount = refundAmount.add(itemTotal);
        }

        if (refundAmount.compareTo(BigDecimal.ZERO) > 0) {
            orderRepository.updateSellerRevenue(sellerId, refundAmount.negate());
        }
    }

    // [修复] 使用 .status() 访问 record 属性
    @Transactional
    public void deleteOrderFromHistory(Long orderId, String sellerId) {
        SellerOrderDTOResponse order = getOrderDetails(orderId, sellerId);
        String status = order.status(); // 修正：getStatus() -> status()

        if (!"DELIVERED".equals(status) && !"CANCELLED".equals(status)) {
            throw new RuntimeException("Only completed or cancelled orders can be removed from history.");
        }

        orderRepository.hideOrderItemsForSeller(orderId, sellerId);
    }
}