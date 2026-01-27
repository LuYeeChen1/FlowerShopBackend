package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.domain.model.ShippingAddress;
import com.backend.flowershop.domain.repository.AddressRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressRepository addressRepository;

    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @GetMapping
    public ResponseEntity<List<ShippingAddress>> getMyAddresses(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(addressRepository.findAllByUserId(jwt.getSubject()));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addAddress(@AuthenticationPrincipal Jwt jwt, @RequestBody ShippingAddress address) {
        if (jwt == null) return ResponseEntity.status(401).build();
        String userId = jwt.getSubject();

        address.setUserId(userId);

        // 如果设置为默认，先清除旧的默认
        if (address.isDefault()) {
            addressRepository.clearDefault(userId);
        }

        addressRepository.save(address);
        return ResponseEntity.ok(Map.of("message", "Address saved"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        if (jwt == null) return ResponseEntity.status(401).build();
        addressRepository.delete(id, jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "Address deleted"));
    }
}