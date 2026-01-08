# Backend Process to File Mapping

本文档将“流程步骤”与“具体文件”一一对应，确保仓库内每个文件都在流程中出现且无多余文件。

---

## 一、运行流程（应用启动与请求处理）

### 1. 应用启动入口
- `src/main/java/com/backend/flowershop/FlowerShopApplication.java`

### 2. 加载基础配置
- `src/main/resources/application.properties`

### 3. 加载开发环境配置（启用 dev profile 时）
- `src/main/resources/application-dev.properties`

### 4. 初始化安全过滤链与 CORS
- `src/main/java/com/backend/flowershop/infrastructure/security/SecurityConfig.java`

### 5. 处理 /me 请求（控制层）
- `src/main/java/com/backend/flowershop/interfaces/controller/MeController.java`

### 6. 构造 JWT Claims 领域模型
- `src/main/java/com/backend/flowershop/domain/model/TokenClaims.java`

### 7. 执行业务用例（获取当前用户 Profile）
- `src/main/java/com/backend/flowershop/application/port/usecase/CurrentUserProfileUseCase.java`
- `src/main/java/com/backend/flowershop/application/service/CurrentUserProfileService.java`

### 8. 生成领域模型输出
- `src/main/java/com/backend/flowershop/domain/model/UserProfile.java`

### 9. 返回响应 DTO
- `src/main/java/com/backend/flowershop/interfaces/controller/dto/UserProfileResponse.java`

---

## 二、构建与运行支撑流程（构建工具链）

### 10. 依赖与构建配置
- `pom.xml`

### 11. Maven Wrapper 启动脚本（Unix）
- `mvnw`

### 12. Maven Wrapper 启动脚本（Windows）
- `mvnw.cmd`

### 13. Maven Wrapper 配置
- `.mvn/wrapper/maven-wrapper.properties`

---

## 三、版本控制与工程规范流程

### 14. 忽略构建产物与 IDE 文件
- `.gitignore`

### 15. 文本换行规范
- `.gitattributes`

---

## 四、规范与说明流程（文档用途）

### 16. 项目冻结规则与范围说明
- `README.md`

### 17. 后端架构分层约束
- `docs/ARCHITECTURE.md`

### 18. 前端整体概览说明（文档用途）
- `docs/FRONTEND_OVERVIEW.md`

### 19. 前端功能与文件映射清单（文档用途）
- `docs/FRONTEND_FEATURE_FILE_MAPPING.md`
