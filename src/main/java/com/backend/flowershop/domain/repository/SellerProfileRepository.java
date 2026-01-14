package com.backend.flowershop.domain.repository;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import java.util.Optional;

/**
 * [Domain Layer] 存储库接口
 * 定义业务所需的持久化契约，隐藏具体的数据库 SQL 实现细节 [cite: 151, 190]。
 */
public interface SellerProfileRepository {

    /**
     * 保存个人花艺师资料 (针对 individual_sellers 表) [cite: 177]
     */
    void saveIndividual(String userId, SellerApplyDTORequest request);

    /**
     * 保存注册商户资料 (针对 business_sellers 表，对齐 E-Invoice) [cite: 180, 182]
     */
    void saveBusiness(String userId, SellerApplyDTORequest request);

    /**
     * 统一查询用户的申请状态 (用于防止重复提交) [cite: 194]
     * @return Optional 包含状态字符串 (如 PENDING_REVIEW, APPROVED)
     */
    Optional<String> findStatusByUserId(String userId);
}