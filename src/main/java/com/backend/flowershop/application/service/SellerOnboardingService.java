package com.backend.flowershop.application.service;

import com.backend.flowershop.application.context.SellerOnboardingContext;
import com.backend.flowershop.application.port.SellerProfileRepository;
import com.backend.flowershop.application.validate.SellerOnboardingValidator;
import com.backend.flowershop.common.exception.ValidationException;
import com.backend.flowershop.domain.model.SellerProfile;
import org.springframework.stereotype.Service;

/**
 * 作用：
 * - 卖家入驻应用服务
 *
 * 职责边界：
 * - 负责协调校验与持久化
 * - 不负责具体规则与数据库实现
 *
 * 使用位置：
 * - 控制器调用
 */
@Service
public class SellerOnboardingService {

    private final SellerOnboardingValidator validator;
    private final SellerProfileRepository repository;

    /**
     * 做什么：
     * - 创建应用服务
     *
     * 输入：
     * - validator：校验器
     * - repository：卖家档案仓储
     *
     * 输出：
     * - 应用服务对象
     */
    public SellerOnboardingService(SellerOnboardingValidator validator,
                                   SellerProfileRepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    /**
     * 做什么：
     * - 提交卖家入驻信息
     *
     * 输入：
     * - context：业务上下文
     *
     * 输出：
     * - 保存后的卖家档案
     */
    public SellerProfile submit(SellerOnboardingContext context) {
        validator.validate(context);
        SellerProfile profile = SellerProfile.create(
                context.getUserId(),
                context.getDisplayName(),
                context.getShopName(),
                context.getDescription()
        );
        return repository.save(profile);
    }

    /**
     * 做什么：
     * - 更新卖家档案
     *
     * 输入：
     * - context：业务上下文
     *
     * 输出：
     * - 更新后的卖家档案
     */
    public SellerProfile update(SellerOnboardingContext context) {
        validator.validate(context);
        SellerProfile profile = SellerProfile.create(
                context.getUserId(),
                context.getDisplayName(),
                context.getShopName(),
                context.getDescription()
        );
        boolean updated = repository.updateByUserId(profile);
        if (!updated) {
            throw new ValidationException("卖家档案不存在");
        }
        return repository.findByUserId(context.getUserId())
                .orElseThrow(() -> new ValidationException("卖家档案不存在"));
    }
}
