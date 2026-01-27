package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.domain.enums.SellerStatus; // 👈 引入
import com.backend.flowershop.domain.enums.SellerType;   // 👈 引入
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
        // ✅ 使用 SellerStatus.ACTIVE.name() 代替 "ACTIVE"
        String sql = """
            INSERT INTO individual_sellers (user_id, real_name, nric_number, phone_number, garden_address, status)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE 
                real_name = VALUES(real_name),
                nric_number = VALUES(nric_number),
                phone_number = VALUES(phone_number),
                garden_address = VALUES(garden_address),
                status = VALUES(status)
        """;
        jdbcTemplate.update(sql, userId, dto.getRealName(), dto.getNricNumber(), dto.getPhoneNumber(), dto.getAddress(),
                SellerStatus.ACTIVE.name()); // 👈 传值
    }

    @Override
    public void saveBusiness(String userId, SellerApplyDTORequest dto) {
        // 使用 SellerStatus.ACTIVE.name() 代替 "ACTIVE"
        String sql = """
            INSERT INTO business_sellers (user_id, company_name, registration_number, tin_number, msic_code, sst_number, phone_number, business_address, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE 
                company_name = VALUES(company_name),
                registration_number = VALUES(registration_number),
                tin_number = VALUES(tin_number),
                msic_code = VALUES(msic_code),
                sst_number = VALUES(sst_number),
                phone_number = VALUES(phone_number),
                business_address = VALUES(business_address),
                status = VALUES(status),
                updated_at = CURRENT_TIMESTAMP
        """;
        jdbcTemplate.update(sql, userId, dto.getCompanyName(), dto.getBrnNumber(), dto.getTinNumber(), dto.getMsicCode(), dto.getSstNumber(), dto.getPhoneNumber(), dto.getAddress(),
                SellerStatus.ACTIVE.name()); // 传值
    }

    // RowMapper 中使用 SellerType.INDIVIDUAL.name() 保持一致性
    private final RowMapper<SellerApplyDTORequest> individualRowMapper = (rs, rowNum) -> {
        SellerApplyDTORequest dto = new SellerApplyDTORequest();
        dto.setApplyType(SellerType.INDIVIDUAL.name()); // 👈 统一
        dto.setRealName(rs.getString("real_name"));
        dto.setNricNumber(rs.getString("nric_number"));
        dto.setPhoneNumber(rs.getString("phone_number"));
        dto.setAddress(rs.getString("garden_address"));
        return dto;
    };

    private final RowMapper<SellerApplyDTORequest> businessRowMapper = (rs, rowNum) -> {
        SellerApplyDTORequest dto = new SellerApplyDTORequest();
        dto.setApplyType(SellerType.BUSINESS.name()); // 👈 统一
        dto.setCompanyName(rs.getString("company_name"));
        // ... (省略其他字段，保持原样)
        return dto;
    };

    @Override
    public Optional<String> findStatusByUserId(String userId) {
        // SQL 不变
        String sql = """
            SELECT status FROM individual_sellers WHERE user_id = ?
            UNION
            SELECT status FROM business_sellers WHERE user_id = ?
        """;
        RowMapper<String> statusRowMapper = (rs, rowNum) -> rs.getString("status");
        return jdbcTemplate.query(sql, statusRowMapper, userId, userId).stream().findFirst();
    }
}