package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.ShippingAddress;
import com.backend.flowershop.domain.repository.AddressRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcAddressRepository implements AddressRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAddressRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ShippingAddress> findAllByUserId(String userId) {
        String sql = "SELECT * FROM shipping_addresses WHERE user_id = ? ORDER BY is_default DESC, created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }

    @Override
    public void save(ShippingAddress address) {
        if (address.getId() == null) {
            String sql = "INSERT INTO shipping_addresses (user_id, recipient_name, phone_number, full_address, is_default) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, address.getUserId(), address.getRecipientName(), address.getPhoneNumber(), address.getFullAddress(), address.isDefault());
        } else {
            String sql = "UPDATE shipping_addresses SET recipient_name=?, phone_number=?, full_address=?, is_default=? WHERE id=? AND user_id=?";
            jdbcTemplate.update(sql, address.getRecipientName(), address.getPhoneNumber(), address.getFullAddress(), address.isDefault(), address.getId(), address.getUserId());
        }
    }

    @Override
    public void delete(Long addressId, String userId) {
        String sql = "DELETE FROM shipping_addresses WHERE id = ? AND user_id = ?";
        jdbcTemplate.update(sql, addressId, userId);
    }

    @Override
    public void clearDefault(String userId) {
        String sql = "UPDATE shipping_addresses SET is_default = false WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    private final RowMapper<ShippingAddress> rowMapper = (rs, rowNum) -> {
        ShippingAddress addr = new ShippingAddress();
        addr.setId(rs.getLong("id"));
        addr.setUserId(rs.getString("user_id"));
        addr.setRecipientName(rs.getString("recipient_name"));
        addr.setPhoneNumber(rs.getString("phone_number"));
        addr.setFullAddress(rs.getString("full_address"));
        addr.setDefault(rs.getBoolean("is_default"));
        return addr;
    };
}