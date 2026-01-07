package com.backend.flowershop.application.normalize.seller;

import com.backend.flowershop.application.normalize.common.EmailNormalizer;
import com.backend.flowershop.application.normalize.common.PhoneNormalizer;
import com.backend.flowershop.domain.model.common.Address;
import com.backend.flowershop.domain.model.common.Email;
import com.backend.flowershop.domain.model.common.PhoneNumber;
import com.backend.flowershop.domain.model.seller.SellerProfile;

/**
 * 作用：SellerProfileNormalizer（Seller 资料标准化）

 * 边界与职责：
 * - 负责对 SellerProfile 中的“可输入字段”做统一格式化
 * - 只做 Normalize，不做任何合法性校验或业务判断

 * 放置原因：
 * - SellerProfile 聚合包含多个字段
 * - 将 Normalize 集中在此，避免 Service / Validator 中到处散落 trim / replace

 * 使用位置：
 * - SubmitSellerOnboardingService
 * - UpdateSellerProfileService

 * 约束：
 * - 不创建/修改 SellerStage（stage 保持不变）
 * - 不做是否允许更新的判断（交由 rules / pipeline）
 */
public final class SellerProfileNormalizer {

    private final EmailNormalizer emailNormalizer = new EmailNormalizer();
    private final PhoneNormalizer phoneNormalizer = new PhoneNormalizer();

    /**
     * 标准化 SellerProfile。
     *
     * 说明：
     * - 返回一个“字段已统一格式”的新 SellerProfile
     * - sellerId / ownerSub / stage 等身份与状态字段保持不变
     *
     * @param profile 原始 SellerProfile
     * @return 标准化后的 SellerProfile
     */
    public SellerProfile normalize(SellerProfile profile) {
        if (profile == null) {
            return null;
        }

        // String：统一 trim（不做空值/合法性判断）
        String shopName = normalizeText(profile.shopName());
        String companyName = normalizeText(profile.companyName());

        // Value Object：交由各自 normalizer
        Email contactEmail = emailNormalizer.normalize(profile.contactEmail());
        PhoneNumber contactPhone = phoneNormalizer.normalize(profile.contactPhone());

        // Address：若需 Normalize，应由 Address 自身/专用 normalizer 负责
        Address businessAddress = profile.businessAddress();

        // documents：此处不做业务校验；若 SellerDocument 需要 Normalize，可未来单独加 SellerDocumentNormalizer
        return new SellerProfile(
                profile.sellerId(),
                profile.ownerSub(),
                shopName,
                companyName,
                contactEmail,
                contactPhone,
                businessAddress,
                profile.stage(),
                profile.documents()
        );
    }

    private String normalizeText(String s) {
        return (s == null) ? null : s.trim();
    }
}
