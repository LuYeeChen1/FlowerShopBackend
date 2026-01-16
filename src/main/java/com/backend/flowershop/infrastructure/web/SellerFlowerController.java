package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
import com.backend.flowershop.application.dto.response.FlowerDTOResponse;
import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.application.service.SellerFlowerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seller/flowers")
public class SellerFlowerController {

    private final SellerFlowerService sellerFlowerService;

    public SellerFlowerController(SellerFlowerService sellerFlowerService) {
        this.sellerFlowerService = sellerFlowerService;
    }

    @GetMapping("/upload-url")
    public ResponseEntity<Map<String, String>> getPresignedUrl(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam("contentType") String contentType,
            @RequestParam("fileName") String fileName
    ) {
        String sellerId = jwt.getSubject();
        URL url = sellerFlowerService.generatePresignedUrl(sellerId, fileName, contentType);
        String key = url.getPath().startsWith("/") ? url.getPath().substring(1) : url.getPath();
        return ResponseEntity.ok(Map.of("uploadUrl", url.toString(), "key", key));
    }

    @PostMapping
    public ResponseEntity<?> createFlower(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody FlowerDTORequest request
    ) {
        String sellerId = jwt.getSubject();
        sellerFlowerService.createFlower(sellerId, request);
        return ResponseEntity.ok("Flower listed successfully.");
    }

    // ✅ 新增：更新商品
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlower(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id,
            @RequestBody FlowerDTORequest request
    ) {
        String sellerId = jwt.getSubject();
        sellerFlowerService.updateFlower(sellerId, id, request);
        return ResponseEntity.ok("Flower updated successfully.");
    }

    // ✅ 新增：删除商品
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlower(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long id
    ) {
        String sellerId = jwt.getSubject();
        sellerFlowerService.deleteFlower(sellerId, id);
        return ResponseEntity.ok("Flower deleted successfully.");
    }

    @GetMapping
    public ResponseEntity<List<FlowerDTOResponse>> getMyInventory(@AuthenticationPrincipal Jwt jwt) {
        String sellerId = jwt.getSubject();
        List<FlowerDTOResponse> myFlowers = sellerFlowerService.getMyInventory(sellerId);
        return ResponseEntity.ok(myFlowers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlowerDetailDTOResponse> getFlowerDetail(@PathVariable Long id) {
        return sellerFlowerService.getFlowerDetail(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}