package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.response.FlowerDTOResponse;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowerService {

    private final FlowerRepository flowerRepository;

    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public FlowerService(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    public List<Flower> getPublicFlowers(String category, String search, int offset, int limit) {
        String filterCategory = "ALL".equalsIgnoreCase(category) ? null : category;
        List<Flower> flowers = flowerRepository.findAllActive(filterCategory, search, limit, offset);

        // 处理图片 URL
        flowers.forEach(this::enrichImageUrl);
        return flowers;
    }

    public int countPublicFlowers(String category, String search) {
        String filterCategory = "ALL".equalsIgnoreCase(category) ? null : category;
        return flowerRepository.countActive(filterCategory, search);
    }

    // 保留旧方法兼容性（可选，若其他地方用到）
    public List<FlowerDTOResponse> getPublicFlowerCatalog() {
        return flowerRepository.findAllPublic()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void enrichImageUrl(Flower flower) {
        String rawKey = flower.getImageUrl();
        if (rawKey != null && !rawKey.startsWith("http")) {
            flower.setImageUrl(s3BaseUrl + rawKey);
        }
    }

    private FlowerDTOResponse toResponse(Flower flower) {
        String fullUrl = flower.getImageUrl();
        if (fullUrl != null && !fullUrl.startsWith("http")) {
            fullUrl = s3BaseUrl + fullUrl;
        }
        return new FlowerDTOResponse(
                String.valueOf(flower.getId()),
                flower.getName(),
                flower.getDescription(),
                flower.getPrice(),
                flower.getStock(),
                fullUrl,
                flower.getCategory()
        );
    }
}