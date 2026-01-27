package com.backend.flowershop.application.port.out;

/**
 * [Clean Architecture - Output Port]
 * 职责：定义权限变更的契约。
 * 业务层只管调用这个接口，不用关心底层是 HTTP 请求还是 SDK 调用。
 */
public interface RoleTransitionPort {
    void promoteToSeller(String userId);
}