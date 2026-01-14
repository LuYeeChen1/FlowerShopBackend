package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.application.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getApplicationStatus(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("sub");

        return sellerService.getStatus(userId)
                .map(status -> ResponseEntity.ok(status))
                .orElse(ResponseEntity.ok("NONE"));
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyForSeller(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody SellerApplyDTORequest request) {
        try {
            String userId = jwt.getClaimAsString("sub");
            sellerService.applyForSeller(userId, request);
            return ResponseEntity.ok("Application submitted and approved successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("提交失败: " + e.getMessage());
        }
    }
}