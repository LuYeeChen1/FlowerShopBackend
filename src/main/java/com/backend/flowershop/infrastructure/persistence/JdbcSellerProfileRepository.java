package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
        // 核心变更：初始状态设为 'ACTIVE' [cite: 23]
        String sql = """
            INSERT INTO individual_sellers (user_id, real_name, nric_number, phone_number, garden_address, status)
            VALUES (?, ?, ?, ?, ?, 'ACTIVE')
            ON DUPLICATE KEY UPDATE 
                real_name = VALUES(real_name),
                nric_number = VALUES(nric_number),
                phone_number = VALUES(phone_number),
                garden_address = VALUES(garden_address),
                status = 'ACTIVE'
        """;
        jdbcTemplate.update(sql, userId, dto.getRealName(), dto.getNricNumber(), dto.getPhoneNumber(), dto.getAddress());
    }

    @Override
    public void saveBusiness(String userId, SellerApplyDTORequest dto) {
        // 核心变更：初始状态设为 'ACTIVE' [cite: 40]
        String sql = """
            INSERT INTO business_sellers (user_id, company_name, registration_number, tin_number, msic_code, sst_number, phone_number, business_address, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE')
            ON DUPLICATE KEY UPDATE 
                company_name = VALUES(company_name),
                registration_number = VALUES(registration_number),
                tin_number = VALUES(tin_number),
                msic_code = VALUES(msic_code),
                sst_number = VALUES(sst_number),
                phone_number = VALUES(phone_number),
                business_address = VALUES(business_address),
                status = 'ACTIVE',
                updated_at = CURRENT_TIMESTAMP
        """;
        jdbcTemplate.update(sql, userId, dto.getCompanyName(), dto.getBrnNumber(), dto.getTinNumber(), dto.getMsicCode(), dto.getSstNumber(), dto.getPhoneNumber(), dto.getAddress());
    }

    /**
     * 定义 RowMapper 以支持更复杂的查询需求并提升代码整洁度 [cite: 53]
     */
    private final RowMapper<SellerApplyDTORequest> individualRowMapper = (rs, rowNum) -> {
        SellerApplyDTORequest dto = new SellerApplyDTORequest();
        dto.setApplyType("INDIVIDUAL");
        dto.setRealName(rs.getString("real_name"));
        dto.setNricNumber(rs.getString("nric_number"));
        dto.setPhoneNumber(rs.getString("phone_number"));
        dto.setAddress(rs.getString("garden_address"));
        return dto;
    };

    private final RowMapper<SellerApplyDTORequest> businessRowMapper = (rs, rowNum) -> {
        SellerApplyDTORequest dto = new SellerApplyDTORequest();
        dto.setApplyType("BUSINESS");
        dto.setCompanyName(rs.getString("company_name"));
        dto.setBrnNumber(rs.getString("registration_number"));
        dto.setTinNumber(rs.getString("tin_number"));
        dto.setMsicCode(rs.getString("msic_code"));
        dto.setSstNumber(rs.getString("sst_number"));
        dto.setPhoneNumber(rs.getString("phone_number"));
        dto.setAddress(rs.getString("business_address"));
        return dto;
    };

    @Override
    public Optional<String> findStatusByUserId(String userId) {
        // 使用 UNION 同时查询两张表的状态
        String sql = """
            SELECT status FROM individual_sellers WHERE user_id = ?
            UNION
            SELECT status FROM business_sellers WHERE user_id = ?
        """;

        // 🔴 使用 RowMapper 进行状态字符串的映射 [cite: 82]
        RowMapper<String> statusRowMapper = (rs, rowNum) -> rs.getString("status");

        return jdbcTemplate.query(sql, statusRowMapper, userId, userId)
                .stream()
                .findFirst();
    }
}