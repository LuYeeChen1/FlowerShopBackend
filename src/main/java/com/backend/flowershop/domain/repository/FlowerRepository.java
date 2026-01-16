package com.backend.flowershop.domain.repository;

import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.domain.model.Flower;
import java.util.List;
import java.util.Optional;

public interface FlowerRepository {
    // ✅ 修正：只接收 Entity，与 DTO 解耦
    // 这个方法将同时负责 Insert (当 id 为空) 和 Update (当 id 有值)
    void save(Flower flower);

    List<Flower> findAllPublic();

    List<Flower> findAllBySellerId(String sellerId);

    // ✅ 必须有这个方法，用于 Service 层先查后改
    Flower findById(Long id);

    Optional<FlowerDetailDTOResponse> findDetailById(Long id);

    int reduceStock(Long flowerId, int quantity);

    void delete(Long id);
}