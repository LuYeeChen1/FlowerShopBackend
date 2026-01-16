package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SellerOrderService {

    private final OrderRepository orderRepository;

    public SellerOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<SellerOrderDTOResponse> getIncomingOrders(String sellerId) {
        return orderRepository.findOrdersBySellerId(sellerId);
    }

    @Transactional
    public void shipOrder(Long orderId) {
        // 简单处理：卖家点击发货后，将订单改为 SHIPPED
        // 注意：如果是混合订单，这里会把整个订单都改成 SHIPPED。
        // 对于 MVP 来说这是可接受的妥协。
        orderRepository.updateStatus(orderId, "SHIPPED");
    }
}