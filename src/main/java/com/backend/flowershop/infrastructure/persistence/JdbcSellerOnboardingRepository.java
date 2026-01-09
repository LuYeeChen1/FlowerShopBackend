package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.port.persistence.SellerOnboardingRepository;
import com.backend.flowershop.domain.model.SellerOnboarding;
import java.sql.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcSellerOnboardingRepository implements SellerOnboardingRepository {
    private static final String INSERT_ONBOARDING = """
            INSERT INTO seller_onboarding
                (id, store_name, contact_name, contact_email, contact_phone, status, created_at)
            VALUES
                (?, ?, ?, ?, ?, ?, ?)
            """;

    private final JdbcTemplate jdbcTemplate;

    public JdbcSellerOnboardingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(SellerOnboarding onboarding) {
        jdbcTemplate.update(
                INSERT_ONBOARDING,
                onboarding.id().value(),
                onboarding.storeName(),
                onboarding.contact().name(),
                onboarding.contact().email(),
                onboarding.contact().phone(),
                onboarding.status().name(),
                Timestamp.from(onboarding.createdAt().toInstant())
        );
    }
}
