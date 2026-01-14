package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class JdbcSellerProfileRepository implements SellerProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcSellerProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveIndividual(String userId, SellerApplyDTORequest dto) {
        String sql = """
            INSERT INTO individual_sellers (user_id, real_name, nric_number, phone_number, garden_address, status)
            VALUES (?, ?, ?, ?, ?, 'APPROVED')
            ON DUPLICATE KEY UPDATE 
                real_name = VALUES(real_name),
                nric_number = VALUES(nric_number),
                status = 'APPROVED'
        """;
        jdbcTemplate.update(sql, userId, dto.getRealName(), dto.getNricNumber(), dto.getPhoneNumber(), dto.getAddress());
    }

    @Override
    public void saveBusiness(String userId, SellerApplyDTORequest dto) {
        String sql = """
            INSERT INTO business_sellers (user_id, company_name, registration_number, tin_number, msic_code, sst_number, phone_number, business_address, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'APPROVED')
            ON DUPLICATE KEY UPDATE 
                company_name = VALUES(company_name),
                status = 'APPROVED'
        """;
        jdbcTemplate.update(sql, userId, dto.getCompanyName(), dto.getBrnNumber(), dto.getTinNumber(), dto.getMsicCode(), dto.getSstNumber(), dto.getPhoneNumber(), dto.getAddress());
    }

    @Override
    public Optional<String> findStatusByUserId(String userId) {
        String sql = """
            SELECT status FROM individual_sellers WHERE user_id = ?
            UNION
            SELECT status FROM business_sellers WHERE user_id = ?
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("status"), userId, userId)
                .stream().findFirst();
    }
}