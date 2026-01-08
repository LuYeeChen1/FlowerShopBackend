package com.backend.flowershop.infrastructure.persistence.seller;

import com.backend.flowershop.application.port.out.seller.SellerOnboardingRepository;
import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作用：SellerOnboardingRepository 的基础实现

 * ️ 最小实现说明（重要）：
 * - 该类名保留为 JdbcRepository 以匹配冻结结构
 * - 但在 Step 3 接本地 MySQL 前，先用“内存存储”保证 MVP 可跑通
 * - Step 3 时再把内部实现替换为真正 JDBC/SQL（文件名与类名不变）
 */
@Component
public class SellerOnboardingJdbcRepository implements SellerOnboardingRepository {

    private final ConcurrentHashMap<String, SellerOnboarding> store = new ConcurrentHashMap<>();

    @Override
    public SellerOnboarding save(SellerOnboarding onboarding) {
        store.put(onboarding.userSub(), onboarding);
        return onboarding;
    }

    @Override
    public Optional<SellerOnboarding> findByUserSub(String userSub) {
        return Optional.ofNullable(store.get(userSub));
    }
}
