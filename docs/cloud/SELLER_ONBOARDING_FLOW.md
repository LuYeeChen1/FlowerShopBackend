# Seller Onboarding Flow

## 总体模式
- 系统自动初筛（Backend）
- ADMIN 人工终审（Frontend + Lambda）

## Seller 提交流程
1. Seller 填写业务资料（不包含 Cognito 已有字段）
2. Backend 校验并写 MySQL：
    - status = PENDING_REVIEW
3. Backend 调用：
   POST /groups/move-to-seller-pending
4. Cognito：
   CUSTOMER → SELLER_PENDING

## Admin 审核流程
1. Admin 登录（必须有 ADMIN group）
2. Backend 查询 MySQL pending 列表
3. Admin 点 approve / reject
4. Backend：
    - 更新 MySQL 状态 + reason
    - 调用对应 Lambda
5. Cognito Group 迁移：
    - approve → SELLER
    - reject  → SELLER_REJECTED

## 关键约束
- Lambda 不知道 MySQL 的存在
- Backend 不直接改 Cognito Group
