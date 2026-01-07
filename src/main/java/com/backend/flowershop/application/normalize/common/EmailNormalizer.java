package com.backend.flowershop.application.normalize.common;

import com.backend.flowershop.domain.model.common.Email;

/**
 * 作用：EmailNormalizer（邮箱字段标准化）

 * 边界与职责：
 * - Normalize 只做“格式统一/去噪”，不做“合法性校验”
 * - 合法性校验（是否为空/是否符合邮箱规则）必须放到 validator / rules

 * 输入/输出：
 * - 输入：可能带空格、大小写不统一的原始字符串
 * - 输出：统一格式后的 Email 值对象所需字符串

 * 规范：
 * - trim：去掉首尾空白
 * - lower：统一转小写（邮箱本地部分理论上可区分大小写，但业务通常统一为小写）
 */
public final class EmailNormalizer {

    /**
     * 标准化邮箱字符串（供构建 Email 值对象前使用）。
     *
     * @param raw 原始邮箱（可能为 null）
     * @return 标准化后的字符串；raw 为 null 则返回 null
     */
    public String normalize(String raw) {
        if (raw == null) {
            return null;
        }
        return raw.trim().toLowerCase();
    }

    /**
     * 便捷方法：对 Email 值对象做标准化映射（可用于“更新资料”场景）。

     * 说明：
     * - 如果 email 为 null，返回 null（由上层决定是否允许）
     *
     * @param email Email 值对象（可为 null）
     * @return 标准化后的 Email（可为 null）
     */
    public Email normalize(Email email) {
        if (email == null) {
            return null;
        }
        return new Email(normalize(email.value()));
    }
}
