package com.backend.flowershop.domain.model.common;

import java.util.Objects;

/**
 * 业务主键值对象。
 * 职责：统一封装业务主键字符串的语义。
 * 边界：不做校验与格式化，仅保存与比较。
 * 输入输出：输入为业务主键字符串，输出为不可变的值对象。
 */
public final class BusinessId {
    private final String value;

    /**
     * 构造业务主键值对象。
     * 职责：承载业务主键字符串。
     * 边界：不做任何合法性校验。
     * 输入输出：输入 value 字符串，输出为 BusinessId 实例。
     */
    public BusinessId(String value) {
        this.value = value;
    }

    /**
     * 获取业务主键字符串。
     * 输入输出：无输入，输出为主键字符串。
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessId that = (BusinessId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
