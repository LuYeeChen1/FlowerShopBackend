package com.backend.flowershop.domain.repository;

import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.domain.model.Flower;
import java.util.List;
import java.util.Optional;

public interface FlowerRepository {
    void save(Flower flower);

    List<Flower> findAllPublic();

    List<Flower> findAllBySellerId(String sellerId);

    Flower findById(Long id);

    Optional<FlowerDetailDTOResponse> findDetailById(Long id);

    int reduceStock(Long flowerId, int quantity);

    void delete(Long id);

    void restoreStock(Long flowerId, int quantity);

    List<Flower> findAllActive(String category, String search, int limit, int offset);

    int countActive(String category, String search);
}