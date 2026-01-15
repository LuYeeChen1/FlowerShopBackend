package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
import com.backend.flowershop.application.dto.response.FlowerDTOResponse;
import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.application.port.out.StoragePort;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Value; // ✅ 引入这个
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SellerFlowerService {

    private final StoragePort storagePort;
    private final FlowerRepository flowerRepository;

    // ✅ 1. 注入 S3 基础域名 (与 Repository 中保持一致)
    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public SellerFlowerService(StoragePort storagePort, FlowerRepository flowerRepository) {
        this.storagePort = storagePort;
        this.flowerRepository = flowerRepository;
    }

    // ... generatePresignedUrl 和 createFlower 保持不变 ...
    public URL generatePresignedUrl(String sellerId, String fileName, String contentType) {
        String key = "flowers/" + sellerId + "/" + UUID.randomUUID() + "_" + fileName;
        return storagePort.generatePresignedUrl(key, contentType);
    }

    @Transactional
    public void createFlower(String sellerId, FlowerDTORequest request) {
        flowerRepository.save(sellerId, request);
    }

    // 获取列表
    public List<FlowerDTOResponse> getMyInventory(String sellerId) {
        return flowerRepository.findAllBySellerId(sellerId)
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    // 获取详情
    public Optional<FlowerDetailDTOResponse> getFlowerDetail(Long id) {
        return flowerRepository.findDetailById(id).map(dto -> {
            // ✅ 2. 详情页也要拼 URL (如果数据库里不是完整链接的话)
            if (dto.getImageUrl() != null && !dto.getImageUrl().startsWith("http")) {
                dto.setImageUrl(s3BaseUrl + dto.getImageUrl());
            }
            return dto;
        });
    }

    // --- Mapper 方法 ---

    // Entity -> Summary Record
    private FlowerDTOResponse mapToSummaryDTO(Flower flower) {
        // ✅ 3. 核心修复：在这里拼接完整 URL
        String fullUrl = flower.getImageUrl();
        if (fullUrl != null && !fullUrl.startsWith("http")) {
            fullUrl = s3BaseUrl + fullUrl;
        }

        return new FlowerDTOResponse(
                String.valueOf(flower.getId()),
                flower.getName(),
                flower.getDescription(),
                flower.getPrice(),
                fullUrl, // 使用拼接后的 URL
                flower.getCategory()
        );
    }
}