# FlowerShop Backend Overview

## 项目简介

FlowerShop 是一个基于 Spring Boot 的后端服务，采用 Clean Architecture 进行分层，
并以 Cognito JWT 作为认证基础。当前仓库聚焦后端实现与架构规范。

## 技术栈与关键能力

- Spring Boot（REST API）
- Clean Architecture 分层结构
- Cognito JWT Resource Server（Audience/Group 校验）
- JDBC + MySQL 持久化
- Rule/Validator 校验体系（集中在 ruleimpl）

## 目录结构

```
src/main/java/com/backend/flowershop
├─ interfaces/      # Web 层（Controller + DTO + Security）
├─ application/     # UseCase、Service、Validator、Pipeline、Rule 接口
├─ domain/          # 领域模型与错误定义
└─ infrastructure/  # 配置、安全、JDBC 持久化等基础设施实现
```

```
src/main/resources
├─ application.properties
└─ application-dev.properties
```

```
src/test/java/com/backend/flowershop
└─ FlowerShopApplicationTests.java
```

## 已实现的主要接口

| 接口 | 方法 | 说明 |
| --- | --- | --- |
| `/health` | GET | 健康检查（匿名可访问） |
| `/me` | GET | 获取当前认证用户信息 |
| `/seller/onboarding/submit` | POST | 提交 Seller Onboarding 信息 |
| `/seller/onboarding/me` | GET | 获取当前用户的 Seller Onboarding 明细 |
| `/seller/onboarding/status` | GET | 获取当前用户的 Seller Onboarding 状态 |

## 认证与授权

- JWT Resource Server 负责解析并校验 Cognito token。
- Audience 校验位于 `infrastructure/security`。
- Cognito Groups 映射到 Spring Security ROLE，供业务层使用。

## 业务流程概览（Seller Onboarding）

1. Controller 负责请求映射与 principal 提取。
2. UseCase 调用 Validator/Rule Pipeline 完成校验。
3. JDBC Repository 进行持久化与查询。
4. 返回 DTO 给调用方。

## 运行与配置

- 配置文件：`src/main/resources/application.properties` 与 `application-dev.properties`
- JDBC 连接由 `JdbcConfig` 与 `ConnectionProvider` 进行管理

## 测试

- 当前仅包含 `FlowerShopApplicationTests` 的基础启动测试
