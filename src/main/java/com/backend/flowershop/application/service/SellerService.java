package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * [Application Layer] 业务逻辑服务
 * 负责接收 DTO，执行业务校验，并编排存储流程 [cite: 157, 158, 188]。
 */
@Service
public class SellerService {

    private final SellerProfileRepository sellerRepository;

    public SellerService(SellerProfileRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * 获取用户申请状态
     */
    public Optional<String> getStatus(String userId) {
        return sellerRepository.findStatusByUserId(userId);
    }

    /**
     * 核心业务：处理卖家申请
     * 使用 @Transactional 确保数据的原子性
     */
    @Transactional
    public void applyForSeller(String userId, SellerApplyDTORequest request) {

        // 1. 幂等性校验：检查是否已有申请记录，防止重复“寄信” [cite: 194]
        Optional<String> existingStatus = sellerRepository.findStatusByUserId(userId);
        if (existingStatus.isPresent()) {
            throw new IllegalStateException("您已提交过申请，当前状态为: " + existingStatus.get());
        }

        // 2. 身份分流逻辑 (Individual vs Business) [cite: 176, 188]
        if ("INDIVIDUAL".equalsIgnoreCase(request.getApplyType())) {

            // 🔴 关键修复：核验前端传来的 nricNumber (原 idCardNumber 报错点)
            if (request.getRealName() == null || request.getNricNumber() == null) {
                throw new IllegalArgumentException("个人申请必须提供真实姓名与 NRIC 编号");
            }
            sellerRepository.saveIndividual(userId, request);

        } else if ("BUSINESS".equalsIgnoreCase(request.getApplyType())) {

            // 企业级字段校验 (E-Invoice 标准) [cite: 182]
            if (request.getCompanyName() == null || request.getBrnNumber() == null || request.getTinNumber() == null) {
                throw new IllegalArgumentException("企业申请必须提供公司名称、BRN 与 TIN 税号");
            }
            sellerRepository.saveBusiness(userId, request);

        } else {
            throw new IllegalArgumentException("无效的申请类型");
        }
    }
}