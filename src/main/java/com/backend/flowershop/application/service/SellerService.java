package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.application.port.out.RoleTransitionPort;
import com.backend.flowershop.domain.enums.Role;
import com.backend.flowershop.domain.enums.SellerStatus;
import com.backend.flowershop.domain.enums.SellerType;
import com.backend.flowershop.domain.model.User;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import com.backend.flowershop.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SellerService {

    private final SellerProfileRepository sellerRepository;
    private final UserRepository userRepository;
    private final RoleTransitionPort roleTransitionPort;

    public SellerService(SellerProfileRepository sellerRepository,
                         UserRepository userRepository,
                         RoleTransitionPort roleTransitionPort) {
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
        this.roleTransitionPort = roleTransitionPort;
    }

    public Optional<String> getStatus(String userId) {
        return sellerRepository.findStatusByUserId(userId);
    }

    /**
     * 核心交易逻辑：
     * 0. 🔥 同步用户基础信息 (Fix FK Error)
     * 1. 检查状态
     * 2. 写入商家资料
     * 3. 调用 Lambda 修改 Cognito
     * 4. 更新本地用户角色
     */
    // 修改签名：接受 email 和 username
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void applyForSeller(String userId, String email, String username, SellerApplyDTORequest request) {

        // ==========================================================
        // 🔍 检查点：你是否漏掉了下面这三行代码？
        // ==========================================================
        System.out.println("正在同步用户到本地数据库: " + userId); // 👈以此确认代码已执行
        User currentUser = new User(userId, email, username, Role.CUSTOMER);
        userRepository.save(currentUser);
        // ==========================================================

        // 1. 幂等性校验
        Optional<String> status = sellerRepository.findStatusByUserId(userId);
        if (status.isPresent() && !SellerStatus.NONE.name().equals(status.get())) {
            throw new IllegalStateException("您已有有效的契约，无法重复提交。");
        }

        // 2. 写入商家资料 (这里就是报错的地方，只要上面执行了，这里就不会报错)
        if (SellerType.INDIVIDUAL.name().equalsIgnoreCase(request.getApplyType())) {
            sellerRepository.saveIndividual(userId, request);
        } else {
            sellerRepository.saveBusiness(userId, request);
        }

        // 3. 触发云端权限变更
        roleTransitionPort.promoteToSeller(userId);

        // 4. 更新本地角色
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
        user.setRole(Role.SELLER);
        userRepository.save(user);
    }
}