package com.backend.flowershop.application.service;

// 引入新的 DTO 类名
import com.backend.flowershop.application.dto.response.UserDTOResponse;
import com.backend.flowershop.domain.model.User;
import com.backend.flowershop.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Use Case: 同步用户
     * 返回类型已更新为: UserDTOResponse
     */
    public UserDTOResponse syncCognitoUser(String id, String email, String username, List<String> cognitoGroups) {
        // 1. 业务逻辑
        String role = "CUSTOMER";
        if (cognitoGroups != null && !cognitoGroups.isEmpty()) {
            role = cognitoGroups.get(0);
        }

        // 2. 领域操作
        User user = new User(id, email, username, role);
        userRepository.save(user);

        // 3. 转换为 Response DTO
        return new UserDTOResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole()
        );
    }
}