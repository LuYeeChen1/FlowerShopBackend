package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.application.service.FlowerService;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.infrastructure.persistence.JdbcFlowerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/flowers")
@CrossOrigin(origins = "http://localhost:5173")
public class PublicFlowerController {

    private final FlowerService flowerService;
    private final JdbcFlowerRepository flowerRepository;

    public PublicFlowerController(FlowerService flowerService, JdbcFlowerRepository flowerRepository) {
        this.flowerService = flowerService;
        this.flowerRepository = flowerRepository;
    }

    // ✅ [更新] 支持分页、分类、搜索
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCatalog(
            @RequestParam(required = false, defaultValue = "ALL") String category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "6") int limit) {

        List<Flower> flowers = flowerService.getPublicFlowers(category, search, offset, limit);
        int total = flowerService.countPublicFlowers(category, search);

        return ResponseEntity.ok(Map.of(
                "list", flowers,
                "total", total
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlowerDetailDTOResponse> getFlowerDetail(@PathVariable Long id) {
        return flowerRepository.findDetailById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}