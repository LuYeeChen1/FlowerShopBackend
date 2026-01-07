package com.backend.flowershop.application.port.out.persistence;

import com.backend.flowershop.domain.model.user.CognitoSub;

import java.util.Optional;

/**
 * 作用：UserProfileRepository（用户资料持久化出口 Port）

 * 边界与职责：
 * - application 层通过该 Port 读写“用户资料”
 * - 这里的“用户资料”指：与业务相关、需要本地数据库保存的最小用户信息
 * - 不包含认证信息（认证由 Cognito 负责）

 * 设计说明：
 * - 当前阶段仅定义最小能力：根据 sub 判断是否存在 / 创建
 * - 未来若需要更多字段（昵称、头像等），再在 domain/model/user 下新增模型并扩展本 Port

 * 约束：
 * - 这是能力声明接口，不包含任何 SQL / 框架代码
 * - 具体实现必须放在 infrastructure/persistence/* 下
 */
public interface UserProfileRepository {

    /**
     * 根据 CognitoSub 查询用户资料是否存在。
     *
     * @param sub Cognito 用户唯一标识
     * @return Optional<UserProfileRecord>（当前阶段使用内部 record 承载最小信息）
     */
    Optional<UserProfileRecord> findBySub(CognitoSub sub);

    /**
     * 创建用户资料（首次出现该 sub）。
     *
     * @param record 最小用户资料
     */
    void create(UserProfileRecord record);

    /**
     * UserProfileRecord（最小用户资料记录）

     * 说明：
     * - 这是 repository 层对 application 的“最小返回形态”
     * - 目前只需要 sub；后续可扩展（例如 email、createdAt）
     * - 不放在 domain：避免在 domain 未冻结新增模型前引入不必要复杂度
     */
    record UserProfileRecord(CognitoSub sub) {
    }
}
