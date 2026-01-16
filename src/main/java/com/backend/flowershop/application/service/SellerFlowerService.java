package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
import com.backend.flowershop.application.dto.response.FlowerDTOResponse;
import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.application.port.out.StoragePort;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public SellerFlowerService(StoragePort storagePort, FlowerRepository flowerRepository) {
        this.storagePort = storagePort;
        this.flowerRepository = flowerRepository;
    }

    public URL generatePresignedUrl(String sellerId, String fileName, String contentType) {
        String key = "flowers/" + sellerId + "/" + UUID.randomUUID() + "_" + fileName;
        return storagePort.generatePresignedUrl(key, contentType);
    }

    // Create: DTO -> Entity 转换在这里进行
    @Transactional
    public void createFlower(String sellerId, FlowerDTORequest request) {
        Flower flower = new Flower();
        flower.setName(request.getName());
        flower.setDescription(request.getDescription());
        flower.setPrice(request.getPrice());
        flower.setStock(request.getStock());
        flower.setImageUrl(request.getImageUrl());
        flower.setCategory(request.getCategory());
        flower.setSellerId(sellerId);

        // Repository 只接收纯净的 Entity
        flowerRepository.save(flower);
    }

    // Update: 先查后改
    @Transactional
    public void updateFlower(String sellerId, Long flowerId, FlowerDTORequest request) {
        Flower flower = flowerRepository.findById(flowerId);
        if (flower == null) {
            throw new RuntimeException("Flower not found");
        }

        // 权限检查
        if (!flower.getSellerId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized: You do not own this flower.");
        }

        // 更新字段
        flower.setName(request.getName());
        flower.setDescription(request.getDescription());
        flower.setPrice(request.getPrice());
        flower.setStock(request.getStock());
        flower.setCategory(request.getCategory());

        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            flower.setImageUrl(request.getImageUrl());
        }

        // 调用 save (JDBC Repository 内部会根据 ID 存在与否执行 UPDATE)
        flowerRepository.save(flower);
    }

    @Transactional
    public void deleteFlower(String sellerId, Long flowerId) {
        Flower flower = flowerRepository.findById(flowerId);
        if (flower == null) {
            throw new RuntimeException("Flower not found");
        }
        if (!flower.getSellerId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized");
        }
        flowerRepository.delete(flowerId);
    }

    public List<FlowerDTOResponse> getMyInventory(String sellerId) {
        return flowerRepository.findAllBySellerId(sellerId)
                .stream()
                .map(this::mapToSummaryDTO)
                .collect(Collectors.toList());
    }

    public Optional<FlowerDetailDTOResponse> getFlowerDetail(Long id) {
        return flowerRepository.findDetailById(id).map(dto -> {
            if (dto.getImageUrl() != null && !dto.getImageUrl().startsWith("http")) {
                dto.setImageUrl(s3BaseUrl + dto.getImageUrl());
            }
            return dto;
        });
    }

    private FlowerDTOResponse mapToSummaryDTO(Flower flower) {
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