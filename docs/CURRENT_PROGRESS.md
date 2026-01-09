# 当前进度锚点（动态）

本节整合了 CURRENT_PROGRESS 的内容，用于告诉你「现在做到哪」。

## 已完成

* Cognito Groups 全部创建
* ADMIN 账号已手动加入 ADMIN group
* API Gateway + JWT Authorizer 已配置
* Lambda：
  * move-to-seller-pending
  * approve-seller（含 Guard）
  * reject-seller（含 Guard）
* S3 Bucket 已创建（私有 + CORS）

## 未冻结

* Seller Onboarding（等待 Frontend + Backend 完成并端到端测试）

## 下一步（由 Codex 完成）

* Frontend Seller Submit (这个由frontend 的 repository 来做，backend的 repository只需专注 backend)
* Backend Seller Onboarding Service
* Admin Review UI + Backend
