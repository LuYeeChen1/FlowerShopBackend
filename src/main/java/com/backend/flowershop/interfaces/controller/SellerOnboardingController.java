package com.backend.flowershop.interfaces.controller;

import com.backend.flowershop.application.context.OperationType;
import com.backend.flowershop.application.context.SellerOnboardingContext;
import com.backend.flowershop.application.service.SellerOnboardingService;
import com.backend.flowershop.domain.model.SellerProfile;
import com.backend.flowershop.interfaces.dto.SellerOnboardingRequest;
import com.backend.flowershop.interfaces.dto.SellerOnboardingResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作用：
 * - 提供卖家入驻相关接口
 *
 * 职责边界：
 * - 负责组装上下文并调用应用服务
 * - 不负责业务校验与持久化
 *
 * 使用位置：
 * - /seller/onboarding/submit 与 /seller/profile 接口
 */
@RestController
@RequestMapping("/seller")
public class SellerOnboardingController {

    private final SellerOnboardingService service;

    /**
     * 做什么：
     * - 创建控制器
     *
     * 输入：
     * - service：应用服务
     *
     * 输出：
     * - 控制器对象
     */
    public SellerOnboardingController(SellerOnboardingService service) {
        this.service = service;
    }

    /**
     * 做什么：
     * - 提交卖家入驻申请
     *
     * 输入：
     * - request：入驻请求
     * - authentication：JWT 认证信息
     *
     * 输出：
     * - 卖家档案响应
     */
    @PostMapping("/onboarding/submit")
    public SellerOnboardingResponse submit(@RequestBody SellerOnboardingRequest request,
                                           JwtAuthenticationToken authentication) {
        SellerOnboardingContext context = buildContext(request, authentication, OperationType.SUBMIT);
        SellerProfile profile = service.submit(context);
        return toResponse(profile);
    }

    /**
     * 做什么：
     * - 更新卖家档案
     *
     * 输入：
     * - request：更新请求
     * - authentication：JWT 认证信息
     *
     * 输出：
     * - 卖家档案响应
     */
    @PutMapping("/profile")
    public SellerOnboardingResponse update(@RequestBody SellerOnboardingRequest request,
                                           JwtAuthenticationToken authentication) {
        SellerOnboardingContext context = buildContext(request, authentication, OperationType.UPDATE);
        SellerProfile profile = service.update(context);
        return toResponse(profile);
    }

    /**
     * 做什么：
     * - 组装卖家入驻上下文
     *
     * 输入：
     * - request：请求 DTO
     * - authentication：认证信息
     * - operationType：操作类型
     *
     * 输出：
     * - 业务上下文
     */
    private SellerOnboardingContext buildContext(SellerOnboardingRequest request,
                                                 JwtAuthenticationToken authentication,
                                                 OperationType operationType) {
        Jwt jwt = authentication.getToken();
        SellerOnboardingContext context = new SellerOnboardingContext();
        context.setUserId(jwt.getSubject());
        context.setUsername(jwt.getClaimAsString("cognito:username"));
        context.setEmail(jwt.getClaimAsString("email"));
        context.setDisplayName(request.getDisplayName());
        context.setShopName(request.getShopName());
        context.setDescription(request.getDescription());
        context.setOperationType(operationType);
        return context;
    }

    /**
     * 做什么：
     * - 转换为响应 DTO
     *
     * 输入：
     * - profile：卖家档案
     *
     * 输出：
     * - 响应 DTO
     */
    private SellerOnboardingResponse toResponse(SellerProfile profile) {
        return new SellerOnboardingResponse(
                profile.getUserId(),
                profile.getDisplayName(),
                profile.getShopName(),
                profile.getDescription(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
