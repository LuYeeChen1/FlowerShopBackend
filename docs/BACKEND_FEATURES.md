# Backend 功能与约定（参考）

本节整合了当前后端已实现功能与执行步骤。

## 1. 应用启动与基础结构

**功能说明**：启动 Spring Boot 应用，装配各层组件（接口层、应用层、基础设施层、领域层）。

**执行步骤**：

1. 通过 `SpringApplication.run` 启动应用。
2. Spring Boot 自动扫描并装配 `@Service`、`@Repository`、`@Component`、`@Configuration` 等组件。

**涉及文件**：

* `src/main/java/com/backend/flowershop/FlowerShopApplication.java`

## 2. 认证与资源服务器（Cognito JWT）

**功能说明**：

* 作为 JWT Resource Server 验证 Cognito access token。
* 校验 token 使用场景（token_use=access）和 client_id。
* 仅允许 `/me` 通过认证访问，其余接口拒绝。
* 支持 CORS 配置。

**执行步骤**：

1. 读取配置（issuer、client_id、jwk-set-uri、CORS 白名单）。
2. 构建 `JwtDecoder` 并加载两类验证器：Issuer 校验 + access token/client_id 校验。
3. SecurityFilterChain 配置：
   * 允许 `OPTIONS` 预检。
   * `/me` 需要登录。
   * 其他接口拒绝。
4. 通过 CORS 配置允许指定来源与方法。

**涉及文件**：

* `src/main/java/com/backend/flowershop/infrastructure/security/SecurityConfig.java`
* `src/main/java/com/backend/flowershop/infrastructure/security/AccessTokenValidator.java`
* `src/main/resources/application-dev.properties`

## 3. 当前用户资料查询（/me）

**功能说明**：

* 提供 `GET /me` 返回当前用户的基本信息（subject、username、email、groups）。
* 从 JWT 中解析用户信息，并补充缺失邮箱。

**执行步骤**：

1. Controller 接收 `GET /me`，从 JWT 中解析 subject、username、email、groups、access token。
2. 调用应用层用例 `CurrentUserProfileUseCase.fetch` 获取用户资料。
3. 应用层检查本地用户表：
   * 若已有 email，直接返回。
   * 若没有 email，尝试从 Cognito UserInfo 拉取并缓存到本地数据库。
4. 返回 `UserProfileResponse` 给前端。

**涉及文件**：

* `src/main/java/com/backend/flowershop/interfaces/controller/MeController.java`
* `src/main/java/com/backend/flowershop/application/service/CurrentUserProfileService.java`
* `src/main/java/com/backend/flowershop/application/port/usecase/CurrentUserProfileUseCase.java`
* `src/main/java/com/backend/flowershop/interfaces/controller/dto/UserProfileResponse.java`
* `src/main/java/com/backend/flowershop/domain/model/TokenClaims.java`
* `src/main/java/com/backend/flowershop/domain/model/UserProfile.java`

## 4. UserInfo 邮箱补全与缓存

**功能说明**：

* 当 JWT 中不含 email 时，调用 Cognito UserInfo API 获取 email。
* 将 email 持久化到本地 MySQL 表（`users`）。

**执行步骤**：

1. `CurrentUserProfileService` 尝试从用户表读取 email。
2. 若未命中：
   * 使用 access token 调用 UserInfo API。
   * 校验 email 非空后写入本地 `users` 表。
3. 返回补全后的 email。

**涉及文件**：

* `src/main/java/com/backend/flowershop/application/service/CurrentUserProfileService.java`
* `src/main/java/com/backend/flowershop/infrastructure/security/CognitoUserInfoClient.java`
* `src/main/java/com/backend/flowershop/application/port/security/UserInfoEmailPort.java`
* `src/main/java/com/backend/flowershop/infrastructure/persistence/JdbcUserAccountRepository.java`
* `src/main/java/com/backend/flowershop/domain/model/UserAccount.java`
* `src/main/resources/application.properties`
* `src/main/resources/application-dev.properties`

## 5. 统一领域错误输出

**功能说明**：

* 捕获领域异常并统一返回错误结构。

**执行步骤**：

1. 领域层抛出 `DomainErrorException`。
2. `DomainErrorHandler` 捕获异常并返回 `ErrorResponse`。

**涉及文件**：

* `src/main/java/com/backend/flowershop/domain/error/DomainErrorException.java`
* `src/main/java/com/backend/flowershop/interfaces/controller/DomainErrorHandler.java`
* `src/main/java/com/backend/flowershop/interfaces/controller/dto/ErrorResponse.java`

## 6. 本地数据库存储（用户表）

**功能说明**：

* 使用 JDBC 访问本地 MySQL `users` 表。
* 支持根据 subject 查询与 upsert 邮箱。

**执行步骤**：

1. `JdbcUserAccountRepository` 使用 SQL 查找 `sub`。
2. 使用 `INSERT ... ON DUPLICATE KEY UPDATE` 进行 upsert。

**涉及文件**：

* `src/main/java/com/backend/flowershop/infrastructure/persistence/JdbcUserAccountRepository.java`
* `src/main/resources/application-dev.properties`
