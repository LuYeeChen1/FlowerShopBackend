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

    /**
     * [卖家功能] 审核买家的取消申请
     */
    @Transactional
    public void auditCancellation(Long orderId, String sellerId, boolean approved) {
        if (approved) {
            // 1. 同意 -> 更新状态
            orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);
            orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.CANCELLED);

            // 2. 查找该卖家在此订单中的所有商品
            List<OrderItem> items = orderRepository.findOrderItemsByOrderIdAndSellerId(orderId, sellerId);

            BigDecimal refundAmount = BigDecimal.ZERO;

            for (OrderItem item : items) {
                // 3. 恢复库存
                flowerRepository.restoreStock(item.getFlowerId(), item.getQuantity());

                // 4. 计算需扣减的收入 (Price * Qty)
                BigDecimal itemTotal = item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity()));
                refundAmount = refundAmount.add(itemTotal);
            }

            // 5. ✅ [新增] 扣减商家收入 (传入负数)
            if (refundAmount.compareTo(BigDecimal.ZERO) > 0) {
                orderRepository.updateSellerRevenue(sellerId, refundAmount.negate());
            }

        } else {
            // 拒绝 -> 恢复为 PAID
            orderRepository.updateStatus(orderId, OrderStatus.PAID);
        }
    }

    @Transactional
    public void deliverOrder(Long orderId, String sellerId) {
        // 1. 更新当前卖家的商品状态
        orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.DELIVERED);

        // 2. 检查全单是否完成 (复用现有查询方法，避免修改 Repository 接口)
        List<OrderItem> allItems = orderRepository.findOrderItemsByOrderId(orderId);

        boolean allDelivered = allItems.stream()
                .allMatch(item -> item.getStatus() == OrderStatus.DELIVERED);

        if (allDelivered) {
            orderRepository.updateStatus(orderId, OrderStatus.DELIVERED);
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

        // ✅ [新增] 扣减商家收入 (传入负数)
        if (refundAmount.compareTo(BigDecimal.ZERO) > 0) {
            orderRepository.updateSellerRevenue(sellerId, refundAmount.negate());
        }
    }
}