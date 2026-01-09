# FlowerShop Project – Overview

本项目是一个 FlowerShop 平台，目标环境为：
- 开发 / 演示 / FYP
- 0 成本（Free Tier）
- 前后端完全分离（REST API）

## 核心原则
- 云端能力优先（Cognito / API Gateway / Lambda / S3）
- Lambda 只做“云端权限动作”，不做业务
- 业务数据存本地 MySQL
- Seller / Admin 等状态 **只由 Cognito Group 表达**

## 当前阶段
- Seller Onboarding 尚未冻结
- 接下来由 Codex 完成 Frontend + Backend 代码
- 本文档集是 Cloud / 权限 / 流程的唯一事实来源
