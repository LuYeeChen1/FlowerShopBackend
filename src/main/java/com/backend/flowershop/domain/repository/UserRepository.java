package com.backend.flowershop.domain.repository;

import com.backend.flowershop.domain.model.User;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(String id);
}