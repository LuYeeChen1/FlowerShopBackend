# API Gateway & Lambda

## API Gateway
- 类型：HTTP API
- 鉴权：JWT Authorizer（Cognito）
- 所有受保护接口都必须走 Authorizer

## Lambda 设计原则
- 只做 Cognito Group 迁移
- 不读 / 不写 MySQL
- 不做业务校验
- 不处理文件
- 不生成 token

## 已存在的 Lambda API

### 1. POST /groups/move-to-seller-pending
用途：
- 当前登录用户自己
- CUSTOMER → SELLER_PENDING

### 2. POST /groups/approve-seller
用途：
- ADMIN 审核别人
- SELLER_PENDING → SELLER

### 3. POST /groups/reject-seller
用途：
- ADMIN 审核别人
- SELLER_PENDING → SELLER_REJECTED

## Lambda Guard（已实现）
- 仅 ADMIN 可 approve / reject
- ADMIN 不可审核自己
- 只能审核 SELLER_PENDING
- 业务组只能存在一个（状态机自检）

## Lambda IAM 权限（最小）
- cognito-idp:AdminAddUserToGroup
- cognito-idp:AdminRemoveUserFromGroup
- cognito-idp:AdminListGroupsForUser
- Resource 锁定到当前 User Pool ARN
