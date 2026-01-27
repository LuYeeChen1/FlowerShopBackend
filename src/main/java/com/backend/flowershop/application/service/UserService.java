package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.response.UserDTOResponse;
import com.backend.flowershop.domain.enums.Role;
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
     * 将 Cognito 的字符串组名转换为内部的 Role 枚举
     */
    public UserDTOResponse syncCognitoUser(String id, String email, String username, List<String> cognitoGroups) {
        // 1. 默认角色
        Role role = Role.CUSTOMER;

        if (cognitoGroups != null && !cognitoGroups.isEmpty()) {
            try {
                String groupName = cognitoGroups.get(0);
                // 🛡️ 加上 toUpperCase()，确保 "seller" 也能被识别为 Role.SELLER
                role = Role.valueOf(groupName.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Warning: Unknown role received from Cognito: " + cognitoGroups.get(0));
            }
        }

        // 2. 创建并保存用户 (User 构造函数已适配 Enum)
        User user = new User(id, email, username, role);
        userRepository.save(user);

        // 3. 返回 DTO (转换回 String 给前端)
        return new UserDTOResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole().name()
        );
    }
}