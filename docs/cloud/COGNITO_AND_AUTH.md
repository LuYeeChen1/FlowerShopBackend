# Cognito & Authentication

## Cognito 基本设置
- 单一 User Pool
- 单一 App Client
- Public Client + PKCE
- Hosted UI 登录
- 使用 access_token 作为后端鉴权 token

## Cognito Groups（冻结）
### 业务组（同一时间只能存在一个）
- CUSTOMER
- SELLER_PENDING
- SELLER
- SELLER_REJECTED

### 权限组（可叠加）
- ADMIN

规则：
- 业务组必须单选
- ADMIN 可与任一业务组共存
- 不使用 Group Precedence
- 不使用 custom attributes / role_stage

## Token 使用约定
- Frontend：
    - 调 API 时统一使用 `Authorization: Bearer <access_token>`
- Backend：
    - 用 access_token 校验身份
    - 如需要 email，可通过 Cognito UserInfo 获取并缓存
