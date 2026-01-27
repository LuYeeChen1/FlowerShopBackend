package com.backend.flowershop.domain.repository;

import com.backend.flowershop.domain.model.ShippingAddress;
import java.util.List;

public interface AddressRepository {
    List<ShippingAddress> findAllByUserId(String userId);
    void save(ShippingAddress address);
    void delete(Long addressId, String userId);
    // 用于设置默认地址时，先将其他地址设为非默认
    void clearDefault(String userId);
}