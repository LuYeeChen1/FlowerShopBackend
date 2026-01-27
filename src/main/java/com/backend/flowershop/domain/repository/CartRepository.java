package com.backend.flowershop.domain.repository;

import com.backend.flowershop.domain.model.CartItem;
import java.util.List;
import java.util.Optional;

public interface CartRepository {
    // 添加商品（如果已存在则增加数量）
    void addToCart(String userId, Long flowerId, int quantity);

    // 查询某人的购物车（包含鲜花详情）
    List<CartItem> findAllByUserId(String userId);

    // 删除某条记录
    void deleteCartItem(Long cartId, String userId);

    // 添加此方法用于精准更新数量
    void updateQuantity(Long cartId, String userId, int quantity);

    // 清空购物车（下单后用）
    void clearCart(String userId);

    void deleteAllByUserId(String userId);
}