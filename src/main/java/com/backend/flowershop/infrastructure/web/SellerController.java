package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.application.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * [Infrastructure Layer] Web 控制器
 * 仅负责处理 DTO 的 HTTP 映射，不包含业务逻辑 [cite: 161, 163, 187]。
 */
@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
     * 获取申请状态：解决 ResponseEntity::ok 歧义错误
     */
    @GetMapping("/status")
    public ResponseEntity<String> getApplicationStatus(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("sub"); // 安全提取 Cognito ID [cite: 187]

        // 使用 Lambda 显式指定 ok(T) 的重载版本，消除编译歧义
        return sellerService.getStatus(userId)
                .map(status -> ResponseEntity.ok(status))
                .orElse(ResponseEntity.ok("NONE"));
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyForSeller(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody SellerApplyDTORequest request
    ) {
        try {
            String userId = jwt.getClaimAsString("sub");
            sellerService.applyForSeller(userId, request);
            return ResponseEntity.ok("Application submitted successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // 409: 业务冲突 (已申请)
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400: 参数错误 (字段缺失)
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("系统处理异常");
        }
    }
}