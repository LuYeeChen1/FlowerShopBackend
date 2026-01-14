package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSellerProfileRepository implements SellerProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcSellerProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveIndividual(String userId, SellerApplyDTORequest dto) {
        // 确保 dto.getPhoneNumber() 在 Controller 层已拼接好前缀
        String sql = """
        INSERT INTO individual_sellers (user_id, real_name, nric_number, phone_number, garden_address, status)
        VALUES (?, ?, ?, ?, ?, 'PENDING_REVIEW')
        ON DUPLICATE KEY UPDATE 
            real_name = VALUES(real_name),
            nric_number = VALUES(nric_number),
            phone_number = VALUES(phone_number),
            garden_address = VALUES(garden_address),
            status = 'PENDING_REVIEW'
    """;
        jdbcTemplate.update(sql, userId, dto.getRealName(), dto.getNricNumber(), dto.getPhoneNumber(), dto.getAddress());
    }

    @Override
    public void saveBusiness(String userId, SellerApplyDTORequest dto) {
        String sql = """
            INSERT INTO business_sellers (user_id, company_name, registration_number, tin_number, msic_code, sst_number, phone_number, business_address, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'PENDING_REVIEW')
            ON DUPLICATE KEY UPDATE 
                company_name = VALUES(company_name),
                registration_number = VALUES(registration_number),
                tin_number = VALUES(tin_number),
                msic_code = VALUES(msic_code),
                sst_number = VALUES(sst_number),
                phone_number = VALUES(phone_number),
                business_address = VALUES(business_address),
                status = 'PENDING_REVIEW',
                updated_at = CURRENT_TIMESTAMP
        """;
        jdbcTemplate.update(sql, userId, dto.getCompanyName(), dto.getBrnNumber(), dto.getTinNumber(), dto.getMsicCode(), dto.getSstNumber(), dto.getPhoneNumber(), dto.getAddress());
    }

    // =========================================================
    // 👇 如果你想要 "查询" 功能 (SELECT)，就需要加上 RowMapper
    // =========================================================

    // 1. 个人花艺师的 Mapper
    private final RowMapper<SellerApplyDTORequest> individualRowMapper = (rs, rowNum) -> {
        SellerApplyDTORequest dto = new SellerApplyDTORequest();
        dto.setApplyType("INDIVIDUAL");
        dto.setRealName(rs.getString("real_name"));
        dto.setNricNumber(rs.getString("nric_number")); // 注意这里读的是数据库字段 nric_number
        dto.setPhoneNumber(rs.getString("phone_number"));
        dto.setAddress(rs.getString("garden_address"));
        // status 字段通常单独处理或放入另一个 DTO
        return dto;
    };

    // 2. 企业商户的 Mapper
    private final RowMapper<SellerApplyDTORequest> businessRowMapper = (rs, rowNum) -> {
        SellerApplyDTORequest dto = new SellerApplyDTORequest();
        dto.setApplyType("BUSINESS");
        dto.setCompanyName(rs.getString("company_name")); // 对应数据库 company_name
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
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("status"), userId, userId)
                .stream().findFirst();
    }
}