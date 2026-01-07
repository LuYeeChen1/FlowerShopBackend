package com.backend.flowershop.application.port.out.security;

import com.backend.flowershop.domain.model.user.RoleGroup;
import com.backend.flowershop.domain.model.user.RoleStage;

/**
 * 作用：AuthorizationPort（授权/权限判断出口 Port）

 * 边界与职责：
 * - application 层需要做“是否允许”的判断，但不能依赖 Spring Security 的注解或实现细节
 * - 这里提供最小、明确的授权能力，供 UseCase/Service/Rule 调用

 * 输入/输出：
 * - 输入：期望的角色组/角色阶段
 * - 输出：boolean 或直接抛出（由实现层决定；此处用 boolean 让规则更清晰）

 * 约束：
 * - 这里只定义能力，不包含任何 JWT/HTTP/框架逻辑
 * - 具体实现必须放在 infrastructure/security/AuthorizationAdapter.java
 */
public interface AuthorizationPort {

    /**
     * 当前用户是否属于指定的角色组。
     *
     * @param required 期望角色组（ADMIN / SELLER / CUSTOMER）
     * @return true=满足；false=不满足
     */
    boolean hasRoleGroup(RoleGroup required);

    /**
     * 当前用户是否处于指定的角色阶段。
     *
     * @param required 期望阶段（CUSTOMER / SELLER_PENDING / ...）
     * @return true=满足；false=不满足
     */
    boolean hasRoleStage(RoleStage required);

    /**
     * 当前用户是否属于“任意一个”角色组（用于允许多个角色访问同一功能）。
     *
     * @param anyOf 允许的角色组列表（至少 1 个）
     * @return true=满足其一；false=全部不满足
     */
    boolean hasAnyRoleGroup(RoleGroup... anyOf);

    /**
     * 当前用户是否处于“任意一个”角色阶段（用于允许多个阶段访问同一功能）。
     *
     * @param anyOf 允许的阶段列表（至少 1 个）
     * @return true=满足其一；false=全部不满足
     */
    boolean hasAnyRoleStage(RoleStage... anyOf);
}
