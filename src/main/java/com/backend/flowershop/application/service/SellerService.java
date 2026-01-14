package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class SellerService {

    private final SellerProfileRepository sellerRepository;

    public SellerService(SellerProfileRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * 修复：补充 getStatus 方法以解决 Controller 编译报错
     */
    public Optional<String> getStatus(String userId) {
        return sellerRepository.findStatusByUserId(userId);
    }

    @Transactional
    public void applyForSeller(String userId, SellerApplyDTORequest request) {
        // 1. 幂等性校验：如果状态不是 NONE，则不允许重复提交
        Optional<String> status = sellerRepository.findStatusByUserId(userId);
        if (status.isPresent() && !"NONE".equals(status.get())) {
            throw new IllegalStateException("您已有有效的契约或被管理员注销资格，无法重复提交。");
        }

        if ("INDIVIDUAL".equalsIgnoreCase(request.getApplyType())) {
            if (request.getRealName() == null || request.getNricNumber() == null) {
                throw new IllegalArgumentException("Real Name and NRIC are required for Individuals");
            }
            sellerRepository.saveIndividual(userId, request);

        } else if ("BUSINESS".equalsIgnoreCase(request.getApplyType())) {
            if (request.getCompanyName() == null || request.getBrnNumber() == null || request.getTinNumber() == null) {
                throw new IllegalArgumentException("Company Name, BRN and TIN are required for Business");
            }
            sellerRepository.saveBusiness(userId, request);

        } else {
            throw new IllegalArgumentException("Invalid Apply Type.");
        }
    }
}