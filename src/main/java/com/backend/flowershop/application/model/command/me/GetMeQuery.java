package com.backend.flowershop.application.model.command.me;

/**
 * 作用：GetMeQuery（获取当前用户信息的查询对象）

 * 边界与职责：
 * - 表达“我要查询 /me”这个意图（Query）
 * - 目前 /me 不需要任何入参，但仍保留 Query 对象：
 *   1) 统一命令/查询模型风格（Command / Query）
 *   2) 未来若扩展（例如 includeProfile=true）不破坏接口形态

 * 设计说明：
 * - 使用 record：不可变、语义清晰
 * - 当前无字段，因此是一个空 record
 */
public record GetMeQuery() {
}
