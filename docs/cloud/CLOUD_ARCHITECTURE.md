# Cloud Architecture

## 使用的 AWS 服务（全部在 Free Tier 内）
- Amazon Cognito（认证 + Group）
- API Gateway HTTP API（REST API）
- AWS Lambda（只做 Cognito Group 迁移）
- Amazon S3（Seller 材料上传，可选）
- CloudWatch Logs（默认，用于排错）

## 不使用的服务（刻意不用）
- 不使用 DynamoDB / RDS
- 不使用 Step Functions
- 不使用 SES / SNS
- 不使用 OCR / AI / 风控类服务

## 总体结构（逻辑）
Frontend
→ API Gateway (JWT Authorizer)
→ Lambda（改 Cognito Group）
→ Backend（Spring Boot / MySQL）

说明：
- Frontend 只通过 API Gateway 访问 Lambda
- Backend 不直接操作 Cognito Group
- Lambda 永远不访问 MySQL
