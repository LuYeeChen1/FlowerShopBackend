# Cloud / Auth / Serverless（按需阅读）

当任务涉及 **云端、认证或 Serverless** 时，阅读以下内容。

## Cloud Architecture

**使用的 AWS 服务（全部在 Free Tier 内）**

* Amazon Cognito（认证 + Group）
* API Gateway HTTP API（REST API）
* AWS Lambda（只做 Cognito Group 迁移）
* Amazon S3（Seller 材料上传，可选）
* CloudWatch Logs（默认，用于排错）

**不使用的服务（刻意不用）**

* 不使用 DynamoDB / RDS
* 不使用 Step Functions
* 不使用 SES / SNS
* 不使用 OCR / AI / 风控类服务

**总体结构（逻辑）**

Frontend
→ API Gateway (JWT Authorizer)
→ Lambda（改 Cognito Group）
→ Backend（Spring Boot / MySQL）

说明：

* Frontend 只通过 API Gateway 访问 Lambda
* Backend 不直接操作 Cognito Group
* Lambda 永远不访问 MySQL

## Cognito & Authentication

**Cognito 基本设置**

* 单一 User Pool
* 单一 App Client
* Public Client + PKCE
* Hosted UI 登录
* 使用 access_token 作为后端鉴权 token

**Cognito Groups（冻结）**

业务组（同一时间只能存在一个）

* CUSTOMER
* SELLER_PENDING
* SELLER
* SELLER_REJECTED

权限组（可叠加）

* ADMIN

规则：

* 业务组必须单选
* ADMIN 可与任一业务组共存
* 不使用 Group Precedence
* 不使用 custom attributes / role_stage

**Token 使用约定**

* Frontend：
  * 调 API 时统一使用 `Authorization: Bearer <access_token>`
* Backend：
  * 用 access_token 校验身份
  * 如需要 email，可通过 Cognito UserInfo 获取并缓存

## API Gateway & Lambda

**API Gateway**

* 类型：HTTP API
* 鉴权：JWT Authorizer（Cognito）
* 所有受保护接口都必须走 Authorizer

**Lambda 设计原则**

* 只做 Cognito Group 迁移
* 不读 / 不写 MySQL
* 不做业务校验
* 不处理文件
* 不生成 token

**已存在的 Lambda API**

1. `POST /groups/move-to-seller-pending`
   * 当前登录用户自己
   * CUSTOMER → SELLER_PENDING
2. `POST /groups/approve-seller`
   * ADMIN 审核别人
   * SELLER_PENDING → SELLER
3. `POST /groups/reject-seller`
   * ADMIN 审核别人
   * SELLER_PENDING → SELLER_REJECTED

**Lambda Guard（已实现）**

* 仅 ADMIN 可 approve / reject
* ADMIN 不可审核自己
* 只能审核 SELLER_PENDING
* 业务组只能存在一个（状态机自检）

**Lambda IAM 权限（最小）**

* cognito-idp:AdminAddUserToGroup
* cognito-idp:AdminRemoveUserFromGroup
* cognito-idp:AdminListGroupsForUser
* Resource 锁定到当前 User Pool ARN

## S3 Design (Optional)

**用途**

* Seller 提供证明材料（图片 / PDF 等）

**设计模式**

* S3 + Pre-signed URL
* 前端直传 S3
* Backend 只负责：
  * 发 presign URL
  * 记录 object key 到 MySQL

**已完成**

* 私有 S3 Bucket 已创建
* Block Public Access 全开启
* Bucket Owner Enforced
* 最小 CORS（localhost）

**尚未完成**

* IAM Policy（发 presign 用）
* presign 接口设计（只设计，不写 Lambda）

**明确不做**

* 不做 OCR
* 不做自动审核
* 不做 S3 事件触发

## Seller Onboarding Flow

**总体模式**

* 系统自动初筛（Backend）
* ADMIN 人工终审（Frontend + Lambda）

**Seller 提交流程**

1. Seller 填写业务资料（不包含 Cognito 已有字段）
2. Backend 校验并写 MySQL：
   * status = PENDING_REVIEW
3. Backend 调用：
   * POST /groups/move-to-seller-pending
4. Cognito：
   * CUSTOMER → SELLER_PENDING

**Admin 审核流程**

1. Admin 登录（必须有 ADMIN group）
2. Backend 查询 MySQL pending 列表
3. Admin 点 approve / reject
4. Backend：
   * 更新 MySQL 状态 + reason
   * 调用对应 Lambda
5. Cognito Group 迁移：
   * approve → SELLER
   * reject  → SELLER_REJECTED

**关键约束**

* Lambda 不知道 MySQL 的存在
* Backend 不直接改 Cognito Group
