package com.backend.flowershop.application.normalize.common;

import com.backend.flowershop.domain.model.common.PhoneNumber;

/**
 * 作用：PhoneNormalizer（电话号码字段标准化）

 * 边界与职责：
 * - Normalize 层只负责“格式统一/去噪”，不做合法性校验
 * - 是否为空、是否符合国家规则等校验，必须放在 validator / rules

 * 输入/输出：
 * - 输入：可能包含空格、破折号、括号等格式字符的原始号码
 * - 输出：统一格式后的 PhoneNumber 值对象所需字符串

 * 规范：
 * - 去除所有非数字字符（+ 号除外，由业务是否允许决定）
 * - 当前阶段采用最保守策略：仅保留数字
 */
public final class PhoneNormalizer {

    /**
     * 标准化电话号码字符串（供构建 PhoneNumber 值对象前使用）。
     *
     * @param raw 原始电话号码（可能为 null）
     * @return 标准化后的字符串；raw 为 null 则返回 null
     */
    public String normalize(String raw) {
        if (raw == null) {
            return null;
        }
        // 仅保留数字字符
        return raw.replaceAll("[^0-9]", "");
    }

    /**
     * 便捷方法：对 PhoneNumber 值对象做标准化映射。

     * 说明：
     * - 如果 phoneNumber 为 null，返回 null（由上层决定是否允许）
     *
     * @param phoneNumber PhoneNumber 值对象（可为 null）
     * @return 标准化后的 PhoneNumber（可为 null）
     */
    public PhoneNumber normalize(PhoneNumber phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return new PhoneNumber(normalize(phoneNumber.value()));
    }
}
