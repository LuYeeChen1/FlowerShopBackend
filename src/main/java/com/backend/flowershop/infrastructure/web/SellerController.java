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
            // 1. 获取 User ID (sub)
            String userId = jwt.getClaimAsString("sub");

            // 2. 🔥 新增：从 Token 提取 Email 和 Username
            // 这些是为了同步写入本地 users 表，解决外键报错问题
            String email = jwt.getClaimAsString("email");
            String username = jwt.getClaimAsString("username");

            // 🛡️ 防御性代码：防止 username 为空 (Cognito 有时放在 cognito:username)
            if (username == null) {
                username = jwt.getClaimAsString("cognito:username");
            }
            // 如果还是空，默认使用 Email 前缀
            if (username == null && email != null) {
                username = email.split("@")[0];
            }

            // 3. 调用更新后的 Service 方法 (传入 4 个参数)
            sellerService.applyForSeller(userId, email, username, request);

            return ResponseEntity.ok("Application submitted and approved successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            // 打印堆栈以便调试
            e.printStackTrace();
            return ResponseEntity.badRequest().body("提交失败: " + e.getMessage());
        }
    }
}