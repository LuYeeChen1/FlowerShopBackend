# S3 Design (Optional)

## 用途
- Seller 提供证明材料（图片 / PDF 等）

## 设计模式
- S3 + Pre-signed URL
- 前端直传 S3
- Backend 只负责：
    - 发 presign URL
    - 记录 object key 到 MySQL

## 已完成
- 私有 S3 Bucket 已创建
- Block Public Access 全开启
- Bucket Owner Enforced
- 最小 CORS（localhost）

## 尚未完成
- IAM Policy（发 presign 用）
- presign 接口设计（只设计，不写 Lambda）

## 明确不做
- 不做 OCR
- 不做自动审核
- 不做 S3 事件触发
