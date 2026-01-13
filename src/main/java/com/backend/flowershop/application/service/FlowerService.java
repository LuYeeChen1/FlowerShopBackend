package com.backend.flowershop.application.service;

// 引入新的 DTO 类名
import com.backend.flowershop.application.dto.response.FlowerDTOResponse;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowerService {

    private final FlowerRepository flowerRepository;

    public FlowerService(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    /**
     * 返回类型已更新为: List<FlowerDTOResponse>
     */
    public List<FlowerDTOResponse> getPublicFlowerCatalog() {
        return flowerRepository.findAllPublic()
                .stream()
                .map(this::toResponse) // 调用下方的辅助方法
                .toList();
    }

    // 辅助映射方法
    private FlowerDTOResponse toResponse(Flower flower) {
        return new FlowerDTOResponse(
                flower.getId(),
                flower.getName(),
                flower.getDescription(),
                flower.getPrice(),
                flower.getImageUrl(),
                flower.getCategory()
        );
    }
}